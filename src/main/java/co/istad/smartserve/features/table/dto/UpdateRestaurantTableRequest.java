package co.istad.smartserve.features.table.dto;

import co.istad.smartserve.features.table.TableStatus;
import jakarta.validation.constraints.Min;

public record UpdateRestaurantTableRequest(
        String tableNumber,
        @Min(value = 1, message = "Capacity must be at least 1")
        Integer capacity,
        TableStatus tableStatus,
        String floorName,
        String zoneName,
        Boolean status
) {
}
