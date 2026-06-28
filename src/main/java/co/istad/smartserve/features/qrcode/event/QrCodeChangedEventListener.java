package co.istad.smartserve.features.qrcode.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class QrCodeChangedEventListener {
    @EventListener
    public void handleQrCodeChanged(QrCodeChangedEvent event) {
        log.info(
                "QR_CODE_EVENT action={}, qrCodeId={}, restaurantId={}, tableId={}, qrValue={}, qrCodeType={}, occurredAt={}",
                event.action(),
                event.qrCodeId(),
                event.restaurantId(),
                event.tableId(),
                event.qrValue(),
                event.qrCodeType(),
                event.occurredAt()
        );
    }
}
