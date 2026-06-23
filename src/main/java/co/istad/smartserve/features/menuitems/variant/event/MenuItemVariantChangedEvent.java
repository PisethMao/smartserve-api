package co.istad.smartserve.features.menuitems.variant.event;

import java.util.UUID;

public record MenuItemVariantChangedEvent(
        UUID menuItemId,
        UUID variantId,
        String action
) {
}
