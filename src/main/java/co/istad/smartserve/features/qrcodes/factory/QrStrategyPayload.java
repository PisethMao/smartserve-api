package co.istad.smartserve.features.qrcodes.factory;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QrStrategyPayload {
    private String content;
    private String type;
    private Integer tableId;
}
