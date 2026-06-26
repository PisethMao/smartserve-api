package co.istad.smartserve.features.table.dto;

import co.istad.smartserve.features.table.TableStatus;

import java.time.Instant;
import java.util.UUID;

public record RestaurantTableResponse(
        UUID id,
        UUID restaurantId,
        String tableNumber,
        Integer capacity,
        TableStatus tableStatus,
        String floorName,
        String zoneName,
        Boolean status,
        Instant createdAt,
        Instant updatedAt
) {
}
