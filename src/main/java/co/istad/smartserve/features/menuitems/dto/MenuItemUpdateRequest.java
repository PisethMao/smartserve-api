package co.istad.smartserve.features.menuitems.dto;

import co.istad.smartserve.features.menuitems.MenuItemAvailabilityStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record MenuItemUpdateRequest(
        UUID categoryId,
        @Size(max = 150, message = "English name must be less than 150 characters")
        String nameEn,
        @Size(max = 150, message = "Khmer name must be less than 150 characters")
        String nameKh,
        String descriptionEn,
        String descriptionKh,
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
        BigDecimal price,
        @Size(max = 500, message = "Image URL must be less than 500 characters")
        String imageUrl,
        @Min(value = 0, message = "Sold limit cannot be negative")
        Integer soldLimit,
        MenuItemAvailabilityStatus availabilityStatus,
        Boolean status
) {
}
