package co.istad.smartserve.features.menuitems.composite;

import co.istad.smartserve.features.menuitems.MenuItem;
import co.istad.smartserve.features.menuitems.dto.PublicMenuNodeResponse;

import java.util.List;

public record MenuItemLeaf(MenuItem item) implements MenuComponent {
    /*
    * Composite pattern is a pattern that allow we to store or keep
    * or put the object as a tree structure.
    */
    @Override
    public PublicMenuNodeResponse toNodeResponse() {
        return new PublicMenuNodeResponse(
                item.getId(),
                "MENU_ITEM",
                item.getNameEn(),
                item.getNameKh(),
                item.getPrice(),
                item.getImageUrl(),
                item.getMenuItemAvailabilityStatus(),
                List.of()
        );
    }
}
