package co.istad.smartserve.features.table.dto;

import co.istad.smartserve.features.table.TableStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateRestaurantTableRequest(
        @NotBlank(message = "Table number is required")
        String tableNumber,
        @Min(value = 1, message = "Capacity must be at least 1")
        Integer capacity,
        TableStatus tableStatus,
        String floorName,
        String zoneName,
        Boolean status
) {
}
