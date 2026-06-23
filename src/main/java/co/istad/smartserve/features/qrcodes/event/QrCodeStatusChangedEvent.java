package co.istad.smartserve.features.qrcodes.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class QrCodeStatusChangedEvent extends ApplicationEvent {
    private final Long qrCodeId;
    private final Boolean status;

    public QrCodeStatusChangedEvent(Object source, Long qrCodeId, Boolean status) {
        super(source);
        this.qrCodeId = qrCodeId;
        this.status = status;
    }
}
