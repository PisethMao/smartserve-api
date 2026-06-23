package co.istad.smartserve.features.menuitems.variant.command;

import co.istad.smartserve.features.menuitems.variant.MenuItemVariantService;

import java.util.UUID;

public class DeleteMenuItemVariantCommand implements MenuItemVariantCommand<Void> {
    private final MenuItemVariantService menuItemVariantService;
    private final UUID variantId;

    public DeleteMenuItemVariantCommand(MenuItemVariantService menuItemVariantService, UUID menuItemId) {
        this.menuItemVariantService = menuItemVariantService;
        this.variantId = menuItemId;
    }

    @Override
    public Void execute() {
        menuItemVariantService.deleteMenuItemVariant(variantId);
        return null;
    }
}
