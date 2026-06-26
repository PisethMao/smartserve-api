package co.istad.smartserve.features.table.pattern;

import co.istad.smartserve.features.restaurant.Restaurant;
import co.istad.smartserve.features.table.RestaurantTable;
import co.istad.smartserve.features.table.dto.CreateRestaurantTableRequest;
import org.springframework.stereotype.Component;

@Component
public class DefaultRestaurantTableFactory implements RestaurantTableFactory {
    @Override
    public RestaurantTable create(Restaurant restaurant, CreateRestaurantTableRequest request) {
        return RestaurantTableBuilder.create()
                .restaurant(restaurant)
                .tableNumber(request.tableNumber())
                .capacity(request.capacity())
                .tableStatus(request.tableStatus())
                .floorName(request.floorName())
                .zoneName(request.zoneName())
                .status(request.status())
                .build();
    }
}
