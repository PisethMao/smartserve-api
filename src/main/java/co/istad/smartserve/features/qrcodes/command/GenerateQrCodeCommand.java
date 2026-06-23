package co.istad.smartserve.features.qrcodes.command;

import co.istad.smartserve.exception.QrCodeGenerationException;
import co.istad.smartserve.exception.ResourceNotFoundException;
import co.istad.smartserve.features.qrcodes.QrCode;
import co.istad.smartserve.features.qrcodes.QrCodeRepository;
import co.istad.smartserve.features.qrcodes.QrGeneratorAdapter;
import co.istad.smartserve.features.qrcodes.composite.QrCodeAbstraction;
import co.istad.smartserve.features.qrcodes.dto.QrCodeRequest;
import co.istad.smartserve.features.qrcodes.factory.QrCodeFactory;
import co.istad.smartserve.features.qrcodes.factory.QrStrategyPayload;
import co.istad.smartserve.features.restaurant.Restaurant;
import co.istad.smartserve.features.restaurant.RestaurantRepository;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Command Pattern for generating QR codes.
 * Handles validation, sanitization, and generation of QR codes.
 */
@Slf4j
@Builder
public class GenerateQrCodeCommand implements QrCodeCommand<QrCode> {
    private final QrCodeRequest request;
    private final QrCodeRepository qrCodeRepository;
    private final RestaurantRepository restaurantRepository;
    private final QrCodeFactory qrCodeFactory;
    private final QrGeneratorAdapter qrGeneratorAdapter;
    private final QrCodeAbstraction qrCodeAbstraction;

    @Override
    public QrCode execute() {
        // Validate restaurant exists
        Restaurant restaurant = restaurantRepository.findById(request.restaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with ID: " + request.restaurantId()));

        // Validate TABLE_QR type requires tableId
        if ("TABLE_QR".equals(request.qrType()) && request.tableId() == null) {
            throw new IllegalArgumentException("tableId is required for TABLE_QR type");
        }

        // Sanitize inputs to prevent injection attacks
        String sanitizedUrl = sanitizeInput(request.qrUrl());
        String sanitizedValue = sanitizeInput(request.qrValue());

        log.debug("Generating QR code for restaurant: {} with type: {}", request.restaurantId(), request.qrType());

        // Check for duplicate QR value
        qrCodeRepository.findByQrValue(sanitizedValue).ifPresent(existing -> {
            throw new IllegalArgumentException("QR value already exists");
        });

        // Use Factory Method Pattern to determine payload
        QrStrategyPayload payload = qrCodeFactory.createPayload(
                request.qrType(),
                sanitizedUrl,
                request.tableId()
        );

        try {
            // Use Adapter Pattern to generate QR bytes
            byte[] qrBytes = qrGeneratorAdapter.generateQrCodeImage(payload.getContent(), 250, 250);

            // Use Bridge Pattern to render/produce final URL or data stream
            String renderedQr = (String) qrCodeAbstraction.produce(qrBytes, sanitizedValue);

            QrCode qrCode = QrCode.builder()
                    .restaurant(restaurant)
                    .tableId(payload.getTableId())
                    .qrValue(sanitizedValue)
                    .qrUrl(renderedQr)
                    .qrType(payload.getType())
                    .status(request.status())
                    .build();

            QrCode saved = qrCodeRepository.save(qrCode);
            log.info("QR code generated successfully. ID: {}, Restaurant: {}", saved.getId(), request.restaurantId());
            return saved;
        } catch (IOException e) {
            log.error("Failed to generate QR code image", e);
            throw new QrCodeGenerationException("Failed to generate QR Code: Image generation failed", e);
        } catch (Exception e) {
            log.error("Unexpected error during QR code generation", e);
            throw new QrCodeGenerationException("Failed to generate QR Code: " + e.getMessage(), e);
        }
    }

    /**
     * Sanitizes input to prevent injection attacks.
     * Removes potentially dangerous characters while preserving URL structure.
     */
    private String sanitizeInput(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        // Remove control characters and dangerous HTML/JS characters
        return input.replaceAll("[<>\"'%;\\\\(\\)&\n\r\t]", "");
    }
}
