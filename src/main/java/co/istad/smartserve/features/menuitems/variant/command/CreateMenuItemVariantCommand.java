package co.istad.smartserve.features.menuitems.variant.command;

import co.istad.smartserve.features.menuitems.variant.MenuItemVariantService;
import co.istad.smartserve.features.menuitems.variant.dto.MenuItemVariantCreateRequest;
import co.istad.smartserve.features.menuitems.variant.dto.MenuItemVariantResponse;

import java.util.UUID;

public class CreateMenuItemVariantCommand implements MenuItemVariantCommand<MenuItemVariantResponse> {
    private final MenuItemVariantService menuItemVariantService;
    private final UUID menuItemId;
    private final MenuItemVariantCreateRequest menuItemVariantCreateRequest;

    public CreateMenuItemVariantCommand(MenuItemVariantService menuItemVariantService, UUID menuItemId, MenuItemVariantCreateRequest menuItemVariantCreateRequest) {
        this.menuItemVariantService = menuItemVariantService;
        this.menuItemId = menuItemId;
        this.menuItemVariantCreateRequest = menuItemVariantCreateRequest;
    }

    @Override
    public MenuItemVariantResponse execute() {
        return menuItemVariantService.createMenuItemVariant(menuItemId, menuItemVariantCreateRequest);
    }
}
