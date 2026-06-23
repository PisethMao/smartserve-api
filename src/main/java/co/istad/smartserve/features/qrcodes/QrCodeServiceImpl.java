package co.istad.smartserve.features.qrcodes;

import co.istad.smartserve.exception.ResourceNotFoundException;
import co.istad.smartserve.features.qrcodes.command.DeactivateQrCodeCommand;
import co.istad.smartserve.features.qrcodes.command.DeleteQrCodeCommand;
import co.istad.smartserve.features.qrcodes.command.GenerateQrCodeCommand;
import co.istad.smartserve.features.qrcodes.command.UpdateQrCodeCommand;
import co.istad.smartserve.features.qrcodes.composite.QrCodeAbstraction;
import co.istad.smartserve.features.qrcodes.dto.QrCodeRequest;
import co.istad.smartserve.features.qrcodes.dto.QrCodeResponse;
import co.istad.smartserve.features.qrcodes.dto.QrCodeUpdateRequest;
import co.istad.smartserve.features.qrcodes.factory.QrCodeFactory;
import co.istad.smartserve.features.restaurant.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Service for managing QR codes with security checks and proper error handling.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QrCodeServiceImpl implements QrCodeService {

    private final QrCodeRepository qrCodeRepository;
    private final RestaurantRepository restaurantRepository;
    private final QrCodeMapper qrCodeMapper;
    private final QrCodeFactory qrCodeFactory;
    private final QrGeneratorAdapter qrGeneratorAdapter;
    private final QrCodeAbstraction qrCodeAbstraction;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Checks if the current user has ownership of the restaurant.
     * SUPER_ADMIN bypasses this check.
     * 
     * @param restaurantId the restaurant ID to check
     * @throws AccessDeniedException if user doesn't own the restaurant
     */
    private void checkRestaurantOwnership(UUID restaurantId) {
        if (restaurantId == null) {
            throw new IllegalArgumentException("Restaurant ID cannot be null");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            log.warn("Unauthenticated access attempt to restaurant: {}", restaurantId);
            throw new AccessDeniedException("User is not authenticated");
        }

        if (auth.getPrincipal() instanceof Jwt jwt) {
            String role = jwt.getClaimAsString("role");
            
            // SUPER_ADMIN can bypass ownership check
            if ("ROLE_SUPER_ADMIN".equals(role)) {
                log.debug("SUPER_ADMIN accessing restaurant: {}", restaurantId);
                return;
            }

            if ("ROLE_RESTAURANT_ADMIN".equals(role)) {
                String userRestaurantId = jwt.getClaimAsString("restaurant_id");
                if (userRestaurantId == null || !userRestaurantId.equals(restaurantId.toString())) {
                    log.warn("RESTAURANT_ADMIN attempted unauthorized access to restaurant: {}", restaurantId);
                    throw new AccessDeniedException("You do not have permission to access this restaurant's data.");
                }
            } else {
                log.warn("User with role {} attempted to access restaurant: {}", role, restaurantId);
                throw new AccessDeniedException("Insufficient permissions to access this restaurant's data.");
            }
        } else {
            log.warn("Non-JWT authentication attempted to access restaurant: {}", restaurantId);
            throw new AccessDeniedException("Invalid authentication method");
        }
    }

    @Override
    @Transactional
    public QrCodeResponse generateQrCode(QrCodeRequest request) {
        log.info("Generating QR code for restaurant: {} with type: {}", request.restaurantId(), request.qrType());
        
        checkRestaurantOwnership(request.restaurantId());
        
        GenerateQrCodeCommand command = GenerateQrCodeCommand.builder()
                .request(request)
                .qrCodeRepository(qrCodeRepository)
                .restaurantRepository(restaurantRepository)
                .qrCodeFactory(qrCodeFactory)
                .qrGeneratorAdapter(qrGeneratorAdapter)
                .qrCodeAbstraction(qrCodeAbstraction)
                .build();

        QrCode qrCode = command.execute();
        return qrCodeMapper.toResponse(qrCode);
    }

    @Override
    @Transactional
    public void deactivateQrCode(Long qrCodeId) {
        if (qrCodeId == null || qrCodeId <= 0) {
            throw new IllegalArgumentException("Invalid QR Code ID: " + qrCodeId);
        }

        log.info("Attempting to deactivate QR code: {}", qrCodeId);

        QrCode qrCode = qrCodeRepository.findById(qrCodeId)
                .orElseThrow(() -> new ResourceNotFoundException("QR Code not found with ID: " + qrCodeId));
        
        checkRestaurantOwnership(qrCode.getRestaurant().getId());

        DeactivateQrCodeCommand command = DeactivateQrCodeCommand.builder()
                .qrCodeId(qrCodeId)
                .qrCodeRepository(qrCodeRepository)
                .eventPublisher(eventPublisher)
                .build();

        command.execute();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QrCodeResponse> getQrCodesByRestaurant(UUID restaurantId, String type, Boolean status, Pageable pageable) {
        if (restaurantId == null) {
            throw new IllegalArgumentException("Restaurant ID cannot be null");
        }

        log.debug("Fetching QR codes for restaurant: {} with filters type: {}, status: {}", 
                restaurantId, type, status);
        checkRestaurantOwnership(restaurantId);
        
        Page<QrCode> qrCodePage = qrCodeRepository.findByRestaurantIdFiltered(restaurantId, type, status, pageable);
        log.debug("Found {} QR codes in current page for restaurant: {}", qrCodePage.getNumberOfElements(), restaurantId);
        
        return qrCodeMapper.toResponsePage(qrCodePage);
    }

    @Override
    @Transactional(readOnly = true)
    public QrCodeResponse getQrCodeById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid QR Code ID: " + id);
        }

        log.debug("Fetching QR code: {}", id);

        QrCode qrCode = qrCodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("QR Code not found with ID: " + id));
        
        checkRestaurantOwnership(qrCode.getRestaurant().getId());
        
        return qrCodeMapper.toResponse(qrCode);
    }

    @Override
    @Transactional
    public QrCodeResponse updateQrCode(Long id, QrCodeUpdateRequest request) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid QR Code ID: " + id);
        }

        log.info("Updating QR code: {}", id);

        QrCode qrCode = qrCodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("QR Code not found with ID: " + id));

        checkRestaurantOwnership(qrCode.getRestaurant().getId());

        UpdateQrCodeCommand command = UpdateQrCodeCommand.builder()
                .qrCodeId(id)
                .request(request)
                .qrCodeRepository(qrCodeRepository)
                .qrCodeFactory(qrCodeFactory)
                .qrGeneratorAdapter(qrGeneratorAdapter)
                .qrCodeAbstraction(qrCodeAbstraction)
                .build();

        QrCode updated = command.execute();
        return qrCodeMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteQrCode(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid QR Code ID: " + id);
        }

        log.info("Deleting QR code: {}", id);

        QrCode qrCode = qrCodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("QR Code not found with ID: " + id));

        checkRestaurantOwnership(qrCode.getRestaurant().getId());

        DeleteQrCodeCommand command = DeleteQrCodeCommand.builder()
                .qrCodeId(id)
                .qrCodeRepository(qrCodeRepository)
                .build();

        command.execute();
    }
}
