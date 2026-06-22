package co.istad.smartserve.features.menuitems.factory;

import co.istad.smartserve.features.category.Category;
import co.istad.smartserve.features.menuitems.MenuItem;
import co.istad.smartserve.features.menuitems.dto.MenuItemCreateRequest;
import co.istad.smartserve.features.restaurant.Restaurant;

public interface MenuItemFactory {
    MenuItem createMenuItem(
            Restaurant restaurant,
            Category category,
            MenuItemCreateRequest createRequest
    );
}
