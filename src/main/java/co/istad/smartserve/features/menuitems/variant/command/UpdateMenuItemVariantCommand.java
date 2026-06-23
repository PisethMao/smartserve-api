package co.istad.smartserve.features.menuitems.variant.command;

import co.istad.smartserve.features.menuitems.variant.MenuItemVariantService;
import co.istad.smartserve.features.menuitems.variant.dto.MenuItemVariantResponse;
import co.istad.smartserve.features.menuitems.variant.dto.MenuItemVariantUpdateRequest;

import java.util.UUID;

public class UpdateMenuItemVariantCommand implements MenuItemVariantCommand<MenuItemVariantResponse> {
    private final MenuItemVariantService menuItemVariantService;
    private final UUID menuItemId;
    private final MenuItemVariantUpdateRequest menuItemVariantUpdateRequest;

    public UpdateMenuItemVariantCommand(MenuItemVariantService menuItemVariantService, UUID menuItemId, MenuItemVariantUpdateRequest menuItemVariantUpdateRequest) {
        this.menuItemVariantService = menuItemVariantService;
        this.menuItemId = menuItemId;
        this.menuItemVariantUpdateRequest = menuItemVariantUpdateRequest;
    }

    @Override
    public MenuItemVariantResponse execute() {
        return menuItemVariantService.updateMenuItemVariant(menuItemId, menuItemVariantUpdateRequest);
    }
}
