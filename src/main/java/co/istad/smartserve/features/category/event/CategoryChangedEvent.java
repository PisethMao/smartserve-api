package co.istad.smartserve.features.category.event;

import java.util.UUID;

public record CategoryChangedEvent(
        UUID restaurantId,
        UUID categoryId,
        String action
) {
}
