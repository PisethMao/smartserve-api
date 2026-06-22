package co.istad.smartserve.features.menuitems.composite;

import co.istad.smartserve.features.category.Category;
import co.istad.smartserve.features.menuitems.dto.PublicMenuNodeResponse;

import java.util.ArrayList;
import java.util.List;

public class MenuCategoryComposite implements MenuComponent {
    private final Category category;
    private final List<MenuComponent> components = new ArrayList<>();

    public MenuCategoryComposite(Category category) {
        this.category = category;
    }

    public void addComponent(MenuComponent component) {
        components.add(component);
    }

    @Override
    public PublicMenuNodeResponse toNodeResponse() {
        return new PublicMenuNodeResponse(
                category.getId(),
                "CATEGORY",
                category.getNameEn(),
                category.getNameKh(),
                null,
                null,
                null,
                components.stream().map(MenuComponent::toNodeResponse).toList()
        );
    }
}



