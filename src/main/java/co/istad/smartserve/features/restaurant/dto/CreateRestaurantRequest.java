package co.istad.smartserve.features.restaurant.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateRestaurantRequest(
        @NotBlank(message = "Restaurant English name is required")
        @Size(max = 150, message = "English name must be less than 150 characters")
        String nameEn,
        @Size(max = 150, message = "Khmer name must be less than 150 characters")
        String nameKh,
        String descriptionEn,
        String descriptionKh,
        @Size(max = 255, message = "Address must be less than 255 characters")
        String address,
        @Size(max = 30, message = "Phone must be less than 30 characters")
        String phone,
        @Email(message = "Email format is invalid")
        @Size(max = 150, message = "Email must be less than 150 characters")
        String email,
        @Size(max = 500, message = "Logo URL must be less than 500 characters")
        String logoUrl,
        Boolean active
) {
}
