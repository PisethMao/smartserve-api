package co.istad.smartserve.features.qrcodes.command;

import co.istad.smartserve.exception.ResourceNotFoundException;
import co.istad.smartserve.features.qrcodes.QrCode;
import co.istad.smartserve.features.qrcodes.QrCodeRepository;
import co.istad.smartserve.features.qrcodes.event.QrCodeStatusChangedEvent;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Command Pattern for deactivating QR codes.
 * Publishes events when QR code status is changed.
 */
@Slf4j
@Builder
public class DeactivateQrCodeCommand implements QrCodeCommand<Void> {
    private final Long qrCodeId;
    private final QrCodeRepository qrCodeRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Void execute() {
        if (qrCodeId == null || qrCodeId <= 0) {
            throw new IllegalArgumentException("Invalid QR Code ID");
        }

        QrCode qrCode = qrCodeRepository.findById(qrCodeId)
                .orElseThrow(() -> new ResourceNotFoundException("QR Code not found with ID: " + qrCodeId));

        // Only deactivate if not already inactive
        if (Boolean.FALSE.equals(qrCode.getStatus())) {
            log.warn("QR Code is already inactive. ID: {}", qrCodeId);
            return null;
        }

        qrCode.setStatus(false);
        qrCodeRepository.save(qrCode);
        
        log.info("QR Code deactivated successfully. ID: {}", qrCodeId);
        eventPublisher.publishEvent(new QrCodeStatusChangedEvent(this, qrCode.getId(), false));
        
        return null;
    }
}
