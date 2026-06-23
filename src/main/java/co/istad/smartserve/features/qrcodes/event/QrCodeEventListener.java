package co.istad.smartserve.features.qrcodes.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Event listener for QR code status changes.
 * Handles cache invalidation and service notifications.
 */
@Component
@Slf4j
public class QrCodeEventListener {

    /**
     * Handles QR code status change events.
     * Logs the event for audit purposes and can trigger cache invalidation.
     * 
     * @param event the QR code status changed event
     */
    @EventListener
    public void handleQrCodeStatusChanged(QrCodeStatusChangedEvent event) {
        if (event == null) {
            log.warn("Received null QR code status changed event");
            return;
        }

        try {
            log.info("QR Code status changed: ID={}, Status={}. Clearing caches or notifying services...",
                    event.getQrCodeId(), event.getStatus());
            
            // Implementation for clearing caches or notifying other services would go here
            // Examples:
            // - Invalidate cache entries
            // - Notify WebSocket clients
            // - Update external systems
            
        } catch (Exception e) {
            log.error("Error handling QR code status changed event for ID: {}", event.getQrCodeId(), e);
            // Don't throw exception to prevent breaking the event handling chain
        }
    }
}
