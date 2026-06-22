package co.istad.smartserve.features.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    List<Category> findByRestaurant_IdAndDeletedFalseOrderByDisplayOrderAscCreatedAtDesc(UUID restaurantId);

    Optional<Category> findByIdAndDeletedFalse(UUID id);

    boolean existsByRestaurant_IdAndNameEnIgnoreCaseAndDeletedFalse(
            UUID restaurantId,
            String nameEn
    );

    boolean existsByRestaurant_IdAndNameEnIgnoreCaseAndIdNotAndDeletedFalse(
            UUID restaurantId,
            String nameEn,
            UUID id
    );

    boolean existsByRestaurant_IdAndDisplayOrderAndDeletedFalse(
            UUID restaurantId,
            Integer displayOrder
    );

    @Query("""
            select coalesce(max(c.displayOrder), 0) + 1
            from Category c
            where c.restaurant.id = :restaurantId
            and c.deleted = false
            """)
    Integer getNextDisplayOrder(UUID restaurantId);
}