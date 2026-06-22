package co.istad.smartserve.features.menuitems.dto;

import co.istad.smartserve.features.menuitems.MenuItemAvailabilityStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PublicMenuNodeResponse(
        UUID id,
        String type,
        String nameEn,
        String nameKh,
        BigDecimal price,
        String imageUrl,
        MenuItemAvailabilityStatus availabilityStatus,
        List<PublicMenuNodeResponse> children
) {
}
