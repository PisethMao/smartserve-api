package co.istad.smartserve.features.category;

import co.istad.smartserve.features.category.dto.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    /*
    * Adapter Pattern can use for allow two interface
    * can interact. But in this we used for converts internal
    * entity shape to API response shape.
    */
    @Mapping(source = "restaurant.id", target = "restaurantId")
    CategoryResponse toResponse(Category category);
}
