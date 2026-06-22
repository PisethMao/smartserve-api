package co.istad.smartserve.features.menuitems.command;

import co.istad.smartserve.features.menuitems.MenuItemService;
import co.istad.smartserve.features.menuitems.dto.MenuItemAvailabilityRequest;
import co.istad.smartserve.features.menuitems.dto.MenuItemResponse;

import java.util.UUID;

public record ChangeMenuItemAvailabilityCommand(UUID menuItemId, MenuItemAvailabilityRequest request) implements MenuItemCommand<MenuItemResponse> {
    @Override
    public MenuItemResponse execute(MenuItemService menuItemService) {
        return menuItemService.changeAvailability(menuItemId, request);
    }
}
