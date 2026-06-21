package co.istad.smartserve.features.category.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UpdateCategoryRequest(
        @NotBlank(message = "English category name is required")
        String nameEn,
        String nameKh,
        String iconUrl,
        @Min(value = 1, message = "Display order must be greater than 0")
        Integer displayOrder,
        Boolean status
) {
}
