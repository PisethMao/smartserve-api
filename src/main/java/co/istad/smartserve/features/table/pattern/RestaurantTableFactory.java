package co.istad.smartserve.features.table.pattern;

import co.istad.smartserve.features.restaurant.Restaurant;
import co.istad.smartserve.features.table.RestaurantTable;
import co.istad.smartserve.features.table.dto.CreateRestaurantTableRequest;

public interface RestaurantTableFactory {
    RestaurantTable create(Restaurant restaurant, CreateRestaurantTableRequest request);
}
