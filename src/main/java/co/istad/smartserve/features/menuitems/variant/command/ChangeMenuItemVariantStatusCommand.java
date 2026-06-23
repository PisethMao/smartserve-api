package co.istad.smartserve.features.menuitems.variant.command;

import co.istad.smartserve.features.menuitems.variant.MenuItemVariantService;
import co.istad.smartserve.features.menuitems.variant.dto.MenuItemVariantResponse;
import co.istad.smartserve.features.menuitems.variant.dto.MenuItemVariantStatusRequest;

import java.util.UUID;

public class ChangeMenuItemVariantStatusCommand implements MenuItemVariantCommand<MenuItemVariantResponse> {
    private final MenuItemVariantService menuItemVariantService;
    private final UUID menuItemId;
    private final MenuItemVariantStatusRequest menuItemVariantStatusRequest;

    public ChangeMenuItemVariantStatusCommand(MenuItemVariantService menuItemVariantService, UUID menuItemId, MenuItemVariantStatusRequest menuItemVariantStatusRequest) {
        this.menuItemVariantService = menuItemVariantService;
        this.menuItemId = menuItemId;
        this.menuItemVariantStatusRequest = menuItemVariantStatusRequest;
    }

    @Override
    public MenuItemVariantResponse execute() {
        return menuItemVariantService.changeStatus(menuItemId, menuItemVariantStatusRequest);
    }
}
