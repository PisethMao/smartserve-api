package co.istad.smartserve.features.menuitems;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenuItemRepository extends JpaRepository<MenuItem, UUID> {

    @Query("""
                SELECT m FROM MenuItem m
                JOIN FETCH m.restaurant r
                JOIN FETCH m.category c
                WHERE m.id = :id
                AND m.deleted = false
            """)
    Optional<MenuItem> findByIdAndNotDeleted(@Param("id") UUID id);

    @Query(
            value = """
                        SELECT m FROM MenuItem m
                        JOIN FETCH m.restaurant r
                        JOIN FETCH m.category c
                        WHERE r.id = :restaurantId
                        AND m.deleted = false
                    """,
            countQuery = """
                        SELECT COUNT(m) FROM MenuItem m
                        WHERE m.restaurant.id = :restaurantId
                        AND m.deleted = false
                    """
    )
    Page<MenuItem> findByRestaurantIdAndNotDeleted(
            @Param("restaurantId") UUID restaurantId,
            Pageable pageable
    );

    @Query(
            value = """
                        SELECT m FROM MenuItem m
                        JOIN FETCH m.restaurant r
                        JOIN FETCH m.category c
                        WHERE r.id = :restaurantId
                        AND c.id = :categoryId
                        AND m.deleted = false
                    """,
            countQuery = """
                        SELECT COUNT(m) FROM MenuItem m
                        WHERE m.restaurant.id = :restaurantId
                        AND m.category.id = :categoryId
                        AND m.deleted = false
                    """
    )
    Page<MenuItem> findByRestaurantIdAndCategoryIdAndNotDeleted(
            @Param("restaurantId") UUID restaurantId,
            @Param("categoryId") UUID categoryId,
            Pageable pageable
    );

    @Query("""
                SELECT COUNT(m) > 0 FROM MenuItem m
                WHERE m.restaurant.id = :restaurantId
                AND m.category.id = :categoryId
                AND LOWER(m.nameEn) = LOWER(:nameEn)
                AND m.deleted = false
            """)
    boolean existsByRestaurantAndCategoryAndNameEn(
            @Param("restaurantId") UUID restaurantId,
            @Param("categoryId") UUID categoryId,
            @Param("nameEn") String nameEn
    );

    @Query("""
                SELECT COUNT(m) > 0 FROM MenuItem m
                WHERE m.restaurant.id = :restaurantId
                AND m.category.id = :categoryId
                AND LOWER(m.nameEn) = LOWER(:nameEn)
                AND m.id <> :menuItemId
                AND m.deleted = false
            """)
    boolean existsByRestaurantAndCategoryAndNameEnExceptSelf(
            @Param("restaurantId") UUID restaurantId,
            @Param("categoryId") UUID categoryId,
            @Param("nameEn") String nameEn,
            @Param("menuItemId") UUID menuItemId
    );

    @Query("""
                SELECT m FROM MenuItem m
                JOIN FETCH m.restaurant r
                JOIN FETCH m.category c
                WHERE r.id = :restaurantId
                AND m.deleted = false
                AND m.status = true
                AND m.menuItemAvailabilityStatus <> :unavailableStatus
                ORDER BY c.nameEn ASC, m.nameEn ASC
            """)
    List<MenuItem> findPublicMenuByRestaurantId(
            @Param("restaurantId") UUID restaurantId,
            @Param("unavailableStatus") MenuItemAvailabilityStatus unavailableStatus
    );
}