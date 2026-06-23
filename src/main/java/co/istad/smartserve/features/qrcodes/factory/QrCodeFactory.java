package co.istad.smartserve.features.qrcodes.factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Factory Method Pattern to instantiate different QR payloads based on qr_type.
 * Supports PUBLIC_MENU and TABLE_QR types.
 */
@Slf4j
@Component
public class QrCodeFactory {

    /**
     * Creates a QR payload based on the type.
     * 
     * @param type the QR type (PUBLIC_MENU or TABLE_QR)
     * @param baseInfo the base URL/information
     * @param tableId the table ID (required for TABLE_QR)
     * @return QrStrategyPayload configured for the type
     * @throws IllegalArgumentException if type is unknown or invalid
     */
    public QrStrategyPayload createPayload(String type, String baseInfo, Integer tableId) {
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("QR type cannot be null or empty");
        }

        if (baseInfo == null || baseInfo.isEmpty()) {
            throw new IllegalArgumentException("Base info cannot be null or empty");
        }

        return switch (type.toUpperCase()) {
            case "PUBLIC_MENU" -> {
                log.debug("Creating PUBLIC_MENU payload");
                yield QrStrategyPayload.builder()
                        .type("PUBLIC_MENU")
                        .content(baseInfo + "/menu")
                        .build();
            }
            case "TABLE_QR" -> {
                if (tableId == null || tableId <= 0) {
                    throw new IllegalArgumentException("Valid tableId is required for TABLE_QR type");
                }
                log.debug("Creating TABLE_QR payload for table: {}", tableId);
                yield QrStrategyPayload.builder()
                        .type("TABLE_QR")
                        .content(baseInfo + "/table/" + tableId)
                        .tableId(tableId)
                        .build();
            }
            default -> {
                log.error("Unknown QR type requested: {}", type);
                throw new IllegalArgumentException("Unknown QR type: " + type + ". Supported types: PUBLIC_MENU, TABLE_QR");
            }
        };
    }
}
