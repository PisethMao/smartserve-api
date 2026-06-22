package co.istad.smartserve.features.menuitems.factory;

import co.istad.smartserve.features.category.Category;
import co.istad.smartserve.features.menuitems.MenuItem;
import co.istad.smartserve.features.menuitems.MenuItemAvailabilityStatus;
import co.istad.smartserve.features.menuitems.dto.MenuItemCreateRequest;
import co.istad.smartserve.features.restaurant.Restaurant;
import org.springframework.stereotype.Component;

@Component
public class DefaultMenuItemFactory implements MenuItemFactory {
    @Override
    public MenuItem createMenuItem(Restaurant restaurant, Category category, MenuItemCreateRequest createRequest) {
        return MenuItem.builder()
                .restaurant(restaurant)
                .category(category)
                .nameEn(createRequest.nameEn())
                .nameKh(createRequest.nameKh())
                .descriptionEn(createRequest.descriptionEn())
                .descriptionKh(createRequest.descriptionKh())
                .price(createRequest.price())
                .imageUrl(createRequest.imageUrl())
                .soldLimit(createRequest.soldLimit())
                .menuItemAvailabilityStatus(
                        createRequest.availabilityStatus() == null
                                ? MenuItemAvailabilityStatus.AVAILABLE
                                : createRequest.availabilityStatus()
                )
                .status(createRequest.status() == null || createRequest.status())
                .build();
    }
}
