package co.istad.smartserve.features.category.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ReorderCategoryRequest(
        @NotNull(message = "Category ID is required")
        UUID categoryId,
        @NotNull(message = "Display order is required")
        @Min(value = 1, message = "Display order must be greater than 0")
        Integer displayOrder
) {
}
