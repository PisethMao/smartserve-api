package co.istad.smartserve.features.restaurant;

import co.istad.smartserve.features.restaurant.dto.CreateRestaurantRequest;
import co.istad.smartserve.features.restaurant.dto.RestaurantResponse;
import co.istad.smartserve.features.restaurant.dto.UpdateRestaurantRequest;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        builder = @org.mapstruct.Builder(disableBuilder = true)
)
public interface RestaurantMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", expression = "java(request.active() == null ? true : request.active())")
    Restaurant toEntity(CreateRestaurantRequest request);

    RestaurantResponse toResponse(Restaurant restaurant);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(UpdateRestaurantRequest request, @MappingTarget Restaurant restaurant);
}