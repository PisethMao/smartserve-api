package co.istad.smartserve.features.menuitems.variant;

import co.istad.smartserve.features.menuitems.variant.dto.MenuItemVariantResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MenuItemVariantMapper {
    @Mapping(source = "menuItem.id", target = "menuItemId")
    MenuItemVariantResponse toResponse(MenuItemVariant menuItemVariant);
}
