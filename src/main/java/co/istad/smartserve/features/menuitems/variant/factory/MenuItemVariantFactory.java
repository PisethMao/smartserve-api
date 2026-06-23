package co.istad.smartserve.features.menuitems.variant.factory;

import co.istad.smartserve.features.menuitems.MenuItem;
import co.istad.smartserve.features.menuitems.variant.MenuItemVariant;
import co.istad.smartserve.features.menuitems.variant.dto.MenuItemVariantCreateRequest;

public interface MenuItemVariantFactory {
    MenuItemVariant createMenuItemVariant(MenuItem menuItem, MenuItemVariantCreateRequest request);
}
