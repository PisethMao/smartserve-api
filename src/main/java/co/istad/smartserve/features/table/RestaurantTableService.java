package co.istad.smartserve.features.table;

import co.istad.smartserve.features.table.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface RestaurantTableService {
    RestaurantTableResponse create(UUID restaurantId, CreateRestaurantTableRequest request);

    Page<RestaurantTableResponse> findByRestaurant(UUID restaurantId, Pageable pageable);

    RestaurantTableResponse findById(UUID tableId);

    RestaurantTableResponse update(UUID tableId, UpdateRestaurantTableRequest request);

    RestaurantTableResponse changeTableStatus(UUID tableId, ChangeTableStatusRequest request);

    RestaurantTableResponse changeActiveStatus(UUID tableId, ChangeRestaurantTableActiveStatusRequest request);

    void delete(UUID tableId);
}
