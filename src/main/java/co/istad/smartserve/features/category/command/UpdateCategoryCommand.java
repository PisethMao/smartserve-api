package co.istad.smartserve.features.category.command;

import java.util.UUID;

public record UpdateCategoryCommand(
        UUID categoryId,
        String nameEn,
        String nameKh,
        String iconUrl,
        Integer displayOrder,
        Boolean status
) {
}
