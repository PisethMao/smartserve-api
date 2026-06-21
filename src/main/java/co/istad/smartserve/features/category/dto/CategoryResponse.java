package co.istad.smartserve.features.category.dto;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record CategoryResponse(
        UUID id,
        UUID restaurantId,
        String nameEn,
        String nameKh,
        String iconUrl,
        Integer displayOrder,
        Boolean status,
        Instant createdAt,
        Instant updatedAt
) {
}
