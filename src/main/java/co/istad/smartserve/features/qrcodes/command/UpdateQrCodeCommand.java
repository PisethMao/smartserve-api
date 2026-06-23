package co.istad.smartserve.features.qrcodes.command;

import co.istad.smartserve.exception.QrCodeGenerationException;
import co.istad.smartserve.exception.ResourceNotFoundException;
import co.istad.smartserve.features.qrcodes.QrCode;
import co.istad.smartserve.features.qrcodes.QrCodeRepository;
import co.istad.smartserve.features.qrcodes.QrGeneratorAdapter;
import co.istad.smartserve.features.qrcodes.composite.QrCodeAbstraction;
import co.istad.smartserve.features.qrcodes.dto.QrCodeUpdateRequest;
import co.istad.smartserve.features.qrcodes.factory.QrCodeFactory;
import co.istad.smartserve.features.qrcodes.factory.QrStrategyPayload;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Command Pattern for updating QR codes.
 * Handles validation, re-generation (if needed), and saving updates.
 */
@Slf4j
@Builder
public class UpdateQrCodeCommand implements QrCodeCommand<QrCode> {
    private final Long qrCodeId;
    private final QrCodeUpdateRequest request;
    private final QrCodeRepository qrCodeRepository;
    private final QrCodeFactory qrCodeFactory;
    private final QrGeneratorAdapter qrGeneratorAdapter;
    private final QrCodeAbstraction qrCodeAbstraction;

    @Override
    public QrCode execute() {
        log.debug("Executing UpdateQrCodeCommand for ID: {}", qrCodeId);

        QrCode qrCode = qrCodeRepository.findById(qrCodeId)
                .orElseThrow(() -> new ResourceNotFoundException("QR Code not found with ID: " + qrCodeId));

        boolean needsRegeneration = false;

        if (request.status() != null) {
            qrCode.setStatus(request.status());
        }

        if (request.qrType() != null && !request.qrType().equals(qrCode.getQrType())) {
            qrCode.setQrType(request.qrType());
            needsRegeneration = true;
        }

        if (request.tableId() != null && !request.tableId().equals(qrCode.getTableId())) {
            qrCode.setTableId(request.tableId());
            needsRegeneration = true;
        }

        if (request.qrUrl() != null && !request.qrUrl().equals(qrCode.getQrUrl())) {
            needsRegeneration = true;
        }

        if (request.qrValue() != null && !request.qrValue().equals(qrCode.getQrValue())) {
            String sanitizedValue = sanitizeInput(request.qrValue());
            // Check for duplicate QR value if it changed
            if (!sanitizedValue.equals(qrCode.getQrValue())) {
                qrCodeRepository.findByQrValue(sanitizedValue).ifPresent(existing -> {
                    throw new IllegalArgumentException("QR value already exists");
                });
                qrCode.setQrValue(sanitizedValue);
                needsRegeneration = true;
            }
        }

        if (needsRegeneration) {
            regenerateQrCode(qrCode, request.qrUrl());
        }

        QrCode updated = qrCodeRepository.save(qrCode);
        log.info("QR Code with ID: {} updated successfully", qrCodeId);
        return updated;
    }

    private void regenerateQrCode(QrCode qrCode, String requestedUrl) {
        String urlToUse = requestedUrl != null ? sanitizeInput(requestedUrl) : qrCode.getQrUrl();
        
        // Use Factory Method Pattern to determine payload
        QrStrategyPayload payload = qrCodeFactory.createPayload(
                qrCode.getQrType(),
                urlToUse,
                qrCode.getTableId()
        );

        try {
            // Use Adapter Pattern to generate QR bytes
            byte[] qrBytes = qrGeneratorAdapter.generateQrCodeImage(payload.getContent(), 250, 250);

            // Use Bridge Pattern to render/produce final URL or data stream
            String renderedQr = (String) qrCodeAbstraction.produce(qrBytes, qrCode.getQrValue());
            
            qrCode.setQrUrl(renderedQr);
        } catch (IOException e) {
            log.error("Failed to re-generate QR code image", e);
            throw new QrCodeGenerationException("Failed to update QR Code: Image generation failed", e);
        } catch (Exception e) {
            log.error("Unexpected error during QR code re-generation", e);
            throw new QrCodeGenerationException("Failed to update QR Code: " + e.getMessage(), e);
        }
    }

    private String sanitizeInput(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.replaceAll("[<>\"'%;\\\\(\\)&\n\r\t]", "");
    }
}
