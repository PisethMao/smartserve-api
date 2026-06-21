package co.istad.smartserve.features.category.command;

import java.util.UUID;

public record CreateCategoryCommand(
        /*
         * Command Pattern used for: controller receive http request
         * dto from client and convert it to command before send it to
         * service layer.
         */
        UUID restaurantId,
        String nameEn,
        String nameKh,
        String iconUrl,
        Integer displayOrder,
        Boolean status
) {
}
