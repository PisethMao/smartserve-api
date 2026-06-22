package co.istad.smartserve.features.menuitems.command;

import co.istad.smartserve.features.menuitems.MenuItemService;

import java.util.UUID;

public record DeleteMenuItemCommand(UUID menuItemId) implements MenuItemCommand<Void> {
    @Override
    public Void execute(MenuItemService menuItemService) {
        menuItemService.delete(menuItemId);
        return null;
    }
}
