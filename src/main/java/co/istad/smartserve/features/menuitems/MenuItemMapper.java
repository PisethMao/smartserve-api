package co.istad.smartserve.features.menuitems;

import co.istad.smartserve.features.menuitems.dto.MenuItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MenuItemMapper {
    @Mapping(source = "restaurant.id", target = "restaurantId")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.nameEn", target = "categoryNameEn")
    @Mapping(source = "category.nameKh", target = "categoryNameKh")
    @Mapping(source = "menuItemAvailabilityStatus", target = "availabilityStatus")
    MenuItemResponse toMenuItemResponse(MenuItem menuItem);
}
