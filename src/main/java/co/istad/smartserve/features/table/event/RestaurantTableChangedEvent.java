package co.istad.smartserve.features.table.event;

import java.util.UUID;

public record RestaurantTableChangedEvent(
        UUID tableId,
        UUID restaurantId,
        String action
) {
}
