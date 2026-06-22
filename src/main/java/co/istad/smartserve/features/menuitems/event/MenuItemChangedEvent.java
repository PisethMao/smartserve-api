package co.istad.smartserve.features.menuitems.event;

import java.util.UUID;

public record MenuItemChangedEvent(
        UUID restaurantId,
        UUID categoryId,
        UUID menuItemId,
        String action
) {
}
