package co.istad.smartserve.features.menuitems.command;

import co.istad.smartserve.features.menuitems.MenuItemService;
import co.istad.smartserve.features.menuitems.dto.MenuItemCreateRequest;
import co.istad.smartserve.features.menuitems.dto.MenuItemResponse;

import java.util.UUID;

public record CreateMenuItemCommand(UUID restaurantId,
                                    MenuItemCreateRequest menuItemCreateRequest) implements MenuItemCommand<MenuItemResponse> {
    @Override
    public MenuItemResponse execute(MenuItemService menuItemService) {
        return menuItemService.create(restaurantId, menuItemCreateRequest);
    }
}
