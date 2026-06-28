package co.istad.smartserve.features.table;

import co.istad.smartserve.features.table.dto.RestaurantTableResponse;
import co.istad.smartserve.features.table.dto.UpdateRestaurantTableRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RestaurantTableMapper {
    @Mapping(source = "restaurant.id", target = "restaurantId")
    RestaurantTableResponse toRestaurantTableResponse(RestaurantTable restaurantTable);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "restaurant", ignore = true)
    void updateFromRequest(
            UpdateRestaurantTableRequest request,
            @MappingTarget RestaurantTable restaurantTable
    );
}