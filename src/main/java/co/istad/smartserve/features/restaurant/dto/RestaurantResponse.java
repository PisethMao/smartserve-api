package co.istad.smartserve.features.restaurant.dto;

import java.time.Instant;
import java.util.UUID;

public record RestaurantResponse(
        UUID id,
        String nameEn,
        String nameKh,
        String descriptionEn,
        String descriptionKh,
        String address,
        String phone,
        String email,
        String logoUrl,
        Boolean active,
        Instant createdAt,
        Instant updatedAt
) {
}
