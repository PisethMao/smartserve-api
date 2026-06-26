package co.istad.smartserve.features.table;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, UUID> {
    @Query("""
           SELECT t FROM RestaurantTable t
           JOIN FETCH t.restaurant r
           WHERE t.id = :id
           AND t.deleted = false
           """)
    Optional<RestaurantTable> findByIdAndDeletedFalse(UUID id);

    @Query("""
           SELECT t FROM RestaurantTable t
           WHERE t.restaurant.id = :restaurantId
           AND t.deleted = false
           ORDER BY t.createdAt DESC
           """)
    Page<RestaurantTable> findByRestaurantIdAndDeletedFalse(
            UUID restaurantId,
            Pageable pageable
    );

    boolean existsByRestaurant_IdAndTableNumberIgnoreCaseAndDeletedFalse(
            UUID restaurantId,
            String tableNumber
    );

    boolean existsByRestaurant_IdAndTableNumberIgnoreCaseAndIdNotAndDeletedFalse(
            UUID restaurantId,
            String tableNumber,
            UUID id
    );
}
