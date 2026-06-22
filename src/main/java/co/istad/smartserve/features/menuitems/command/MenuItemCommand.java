package co.istad.smartserve.features.menuitems.command;

import co.istad.smartserve.features.menuitems.MenuItemService;

public interface MenuItemCommand<R> {
    R execute(MenuItemService menuItemService);
}
