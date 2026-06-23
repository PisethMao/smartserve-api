package co.istad.smartserve.features.qrcodes.command;

import co.istad.smartserve.exception.ResourceNotFoundException;
import co.istad.smartserve.features.qrcodes.QrCode;
import co.istad.smartserve.features.qrcodes.QrCodeRepository;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

/**
 * Command Pattern for deleting QR codes.
 * Ensures the QR code exists before deletion.
 */
@Slf4j
@Builder
public class DeleteQrCodeCommand implements QrCodeCommand<Void> {
    private final Long qrCodeId;
    private final QrCodeRepository qrCodeRepository;

    @Override
    public Void execute() {
        log.debug("Executing DeleteQrCodeCommand for ID: {}", qrCodeId);
        
        QrCode qrCode = qrCodeRepository.findById(qrCodeId)
                .orElseThrow(() -> new ResourceNotFoundException("QR Code not found with ID: " + qrCodeId));
        
        qrCodeRepository.delete(qrCode);
        log.info("QR Code with ID: {} deleted successfully", qrCodeId);
        
        return null;
    }
}
