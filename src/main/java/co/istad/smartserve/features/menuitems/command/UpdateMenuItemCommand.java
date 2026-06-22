package co.istad.smartserve.features.menuitems.command;

import co.istad.smartserve.features.menuitems.MenuItemService;
import co.istad.smartserve.features.menuitems.dto.MenuItemResponse;
import co.istad.smartserve.features.menuitems.dto.MenuItemUpdateRequest;

import java.util.UUID;

public record UpdateMenuItemCommand(UUID restaurantId,
                                    MenuItemUpdateRequest menuItemUpdateRequest) implements MenuItemCommand<MenuItemResponse> {
    @Override
    public MenuItemResponse execute(MenuItemService menuItemService) {
        return menuItemService.update(restaurantId, menuItemUpdateRequest);
    }
}
