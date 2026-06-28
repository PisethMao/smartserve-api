package co.istad.smartserve.features.qrcode;

import co.istad.smartserve.exception.ConflictException;
import co.istad.smartserve.exception.ResourceNotFoundException;
import co.istad.smartserve.features.qrcode.command.CreateQrCodeCommand;
import co.istad.smartserve.features.qrcode.command.QrCodeCommandInvoker;
import co.istad.smartserve.features.qrcode.command.UpdateQrCodeStatusCommand;
import co.istad.smartserve.features.qrcode.dto.CreateQrCodeRequest;
import co.istad.smartserve.features.qrcode.dto.QrCodeResponse;
import co.istad.smartserve.features.qrcode.dto.UpdateQrCodeStatusRequest;
import co.istad.smartserve.features.qrcode.event.QrCodeChangedEvent;
import co.istad.smartserve.features.qrcode.factory.QrCodeFactory;
import co.istad.smartserve.features.restaurant.Restaurant;
import co.istad.smartserve.features.restaurant.RestaurantRepository;
import co.istad.smartserve.features.table.RestaurantTable;
import co.istad.smartserve.features.table.RestaurantTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class QrCodeServiceImpl implements QrCodeService {
    private final QrCodeRepository qrCodeRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantTableRepository restaurantTableRepository;
    private final QrCodeMapper qrCodeMapper;
    private final QrGeneratorAdapter qrGeneratorAdapter;
    private final QrCodeFactory qrCodeFactory;
    private final ApplicationEventPublisher eventPublisher;
    private final QrCodeCommandInvoker qrCodeCommandInvoker;

    private Restaurant findRestaurantOrThrow(UUID restaurantId) {
        return restaurantRepository.findByIdAndDeletedFalse(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant was not found"));
    }

    private String generateUniqueQrValue() {
        int maxAttempts = 5;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            String qrValue = qrGeneratorAdapter.generateQrValue();
            if (!qrCodeRepository.existsByQrValueAndDeletedFalse(qrValue)) {
                return qrValue;
            }
        }
        throw new ConflictException("Failed to generate unique QR value. Please try again.");
    }

    private void publishQrCodeChangedEvent(QrCode qrCode, String action) {
        eventPublisher.publishEvent(
                new QrCodeChangedEvent(
                        qrCode.getId(),
                        qrCode.getRestaurant().getId(),
                        qrCode.getRestaurantTable() != null
                                ? qrCode.getRestaurantTable().getId()
                                : null,
                        qrCode.getQrValue(),
                        qrCode.getQrCodeType(),
                        action,
                        Instant.now()
                )
        );
    }

    private QrCodeResponse createPublicMenuQr(
            Restaurant restaurant,
            CreateQrCodeRequest request
    ) {
        if (request.tableId() != null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Public menu QR must not have tableId"
            );
        }
        boolean publicMenuQrAlreadyExists =
                qrCodeRepository.existsByRestaurant_IdAndQrCodeTypeAndDeletedFalse(
                        restaurant.getId(),
                        QrCodeType.PUBLIC_MENU
                );
        if (publicMenuQrAlreadyExists) {
            throw new ConflictException(
                    "Public menu QR already exists for this restaurant",
                    Map.of("qrCodeType", "Only one public menu QR is allowed per restaurant")
            );
        }
        String qrValue = generateUniqueQrValue();
        String qrUrl = qrGeneratorAdapter.generatePublicMenuUrl(qrValue);
        QrCode qrCode = qrCodeFactory.createPublicMenuQr(
                restaurant,
                qrValue,
                qrUrl
        );
        QrCode savedQrCode = qrCodeRepository.save(qrCode);
        publishQrCodeChangedEvent(savedQrCode, "CREATED");
        return qrCodeMapper.toResponse(savedQrCode);
    }

    private QrCodeResponse createTableQr(
            Restaurant restaurant,
            CreateQrCodeRequest request
    ) {
        if (request.tableId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "tableId is required for table QR"
            );
        }
        RestaurantTable restaurantTable = restaurantTableRepository
                .findByIdAndDeletedFalse(request.tableId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant table was not found"));
        if (!restaurantTable.getRestaurant().getId().equals(restaurant.getId())) {
            throw new ConflictException(
                    "Table does not belong to this restaurant",
                    Map.of("tableId", "Selected table belongs to another restaurant")
            );
        }
        boolean tableQrAlreadyExists =
                qrCodeRepository.existsByRestaurant_IdAndRestaurantTable_IdAndDeletedFalse(
                        restaurant.getId(),
                        restaurantTable.getId()
                );
        if (tableQrAlreadyExists) {
            throw new ConflictException(
                    "QR code already exists for this table",
                    Map.of("tableId", "Only one QR code is allowed per table")
            );
        }
        String qrValue = generateUniqueQrValue();
        String qrUrl = qrGeneratorAdapter.generateTableQrUrl(qrValue);
        QrCode qrCode = qrCodeFactory.createTableQr(
                restaurant,
                restaurantTable,
                qrValue,
                qrUrl
        );
        QrCode savedQrCode = qrCodeRepository.save(qrCode);
        publishQrCodeChangedEvent(savedQrCode, "CREATED");
        return qrCodeMapper.toResponse(savedQrCode);
    }

    @Override
    public QrCodeResponse createQrCode(UUID restaurantId, CreateQrCodeRequest request) {
        return qrCodeCommandInvoker.execute(
                new CreateQrCodeCommand(() -> {
                    Restaurant restaurant = findRestaurantOrThrow(restaurantId);
                    if (request.qrCodeType() == QrCodeType.PUBLIC_MENU) {
                        return createPublicMenuQr(restaurant, request);
                    }
                    if (request.qrCodeType() == QrCodeType.TABLE_QR) {
                        return createTableQr(restaurant, request);
                    }
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Unsupported QR code type"
                    );
                })
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QrCodeResponse> getQrCodesByRestaurant(UUID restaurantId, Pageable pageable) {
        findRestaurantOrThrow(restaurantId);
        return qrCodeRepository.findByRestaurantIdAndDeletedFalse(
                restaurantId, pageable
        ).map(qrCodeMapper::toResponse);
    }

    private QrCode findQrCodeOrThrow(UUID qrCodeId) {
        return qrCodeRepository.findByIdAndDeletedFalse(qrCodeId)
                .orElseThrow(() -> new ResourceNotFoundException("QR code was not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public QrCodeResponse getQrCodeById(UUID qrCodeId) {
        QrCode qrCode = findQrCodeOrThrow(qrCodeId);
        return qrCodeMapper.toResponse(qrCode);
    }

    @Override
    @Transactional(readOnly = true)
    public QrCodeResponse getQrCodeByValue(String qrValue) {
        QrCode qrCode = qrCodeRepository.findByQrValueAndDeletedFalse(qrValue)
                .orElseThrow(() -> new ResourceNotFoundException("QR code was not found"));
        if (!Boolean.TRUE.equals(qrCode.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "QR code is inactive"
            );
        }
        return qrCodeMapper.toResponse(qrCode);
    }

    @Override
    public QrCodeResponse updateQrCodeStatus(
            UUID qrCodeId,
            UpdateQrCodeStatusRequest request
    ) {
        return qrCodeCommandInvoker.execute(
                new UpdateQrCodeStatusCommand(() -> {
                    QrCode qrCode = findQrCodeOrThrow(qrCodeId);
                    qrCode.setStatus(request.status());
                    QrCode savedQrCode = qrCodeRepository.save(qrCode);
                    publishQrCodeChangedEvent(savedQrCode, "STATUS_CHANGED");
                    return qrCodeMapper.toResponse(savedQrCode);
                })
        );
    }

    @Override
    public void deleteQrCode(UUID qrCodeId) {
        QrCode qrCode = findQrCodeOrThrow(qrCodeId);
        qrCode.setDeleted(true);
        QrCode savedQrCode = qrCodeRepository.save(qrCode);
        publishQrCodeChangedEvent(savedQrCode, "DELETED");
    }
}
