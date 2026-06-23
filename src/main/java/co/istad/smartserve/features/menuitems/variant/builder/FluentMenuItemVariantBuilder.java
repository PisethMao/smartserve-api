package co.istad.smartserve.features.menuitems.variant.builder;

import co.istad.smartserve.features.menuitems.MenuItem;
import co.istad.smartserve.features.menuitems.MenuItemAvailabilityStatus;
import co.istad.smartserve.features.menuitems.variant.MenuItemVariant;

import java.math.BigDecimal;

public class FluentMenuItemVariantBuilder {
    /*
     * Fluent Builder Pattern: This is the modern builder style.
     */
    private MenuItem menuItem;
    private String nameEn;
    private String nameKh;
    private BigDecimal price;
    private Integer displayOrder;
    private MenuItemAvailabilityStatus availabilityStatus;
    private Boolean defaultVariant;

    private FluentMenuItemVariantBuilder() {
    }

    public static FluentMenuItemVariantBuilder aFluentMenuItemVariant() {
        return new FluentMenuItemVariantBuilder();
    }

    public FluentMenuItemVariantBuilder withMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
        return this;
    }

    public FluentMenuItemVariantBuilder withNameEn(String nameEn) {
        this.nameEn = nameEn;
        return this;
    }

    public FluentMenuItemVariantBuilder withNameKh(String nameKh) {
        this.nameKh = nameKh;
        return this;
    }

    public FluentMenuItemVariantBuilder withPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public FluentMenuItemVariantBuilder withDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
        return this;
    }

    public FluentMenuItemVariantBuilder withAvailabilityStatus(MenuItemAvailabilityStatus availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
        return this;
    }

    public FluentMenuItemVariantBuilder withDefaultVariant(Boolean defaultVariant) {
        this.defaultVariant = defaultVariant;
        return this;
    }

    public MenuItemVariant build() {
        MenuItemVariant menuItemVariant = new MenuItemVariant();
        menuItemVariant.setMenuItem(menuItem);
        menuItemVariant.setNameEn(nameEn);
        menuItemVariant.setNameKh(nameKh);
        menuItemVariant.setPrice(price);
        menuItemVariant.setDisplayOrder(displayOrder == null ? 0 : displayOrder);
        menuItemVariant.setAvailabilityStatus(
                availabilityStatus == null
                        ? MenuItemAvailabilityStatus.AVAILABLE
                        : availabilityStatus
        );
        menuItemVariant.setDefaultVariant(defaultVariant != null && defaultVariant);
        return menuItemVariant;
    }
}
