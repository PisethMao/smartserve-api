package co.istad.smartserve.features.restaurant;

import co.istad.smartserve.features.restaurant.dto.CreateRestaurantRequest;
import co.istad.smartserve.features.restaurant.dto.RestaurantResponse;
import co.istad.smartserve.features.restaurant.dto.UpdateRestaurantRequest;

import java.util.List;
import java.util.UUID;

public interface RestaurantService {
    RestaurantResponse createRestaurant(CreateRestaurantRequest request);

    List<RestaurantResponse> getRestaurants();

    RestaurantResponse getRestaurantById(UUID id);

    RestaurantResponse updateRestaurant(UUID id, UpdateRestaurantRequest request);

    void deleteRestaurant(UUID id);
}
