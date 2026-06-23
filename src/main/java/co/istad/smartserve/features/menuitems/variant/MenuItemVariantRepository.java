package co.istad.smartserve.features.menuitems.variant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface MenuItemVariantRepository extends JpaRepository<MenuItemVariant, UUID> {
    @Query("""
                SELECT v FROM MenuItemVariant v
                JOIN FETCH v.menuItem m
                WHERE v.id = :id
                AND v.deleted = false
            """)
    Optional<MenuItemVariant> findByIdAndNotDeleted(@Param("id") UUID id);

    @Query("""
                SELECT v FROM MenuItemVariant v
                WHERE v.menuItem.id = :menuItemId
                AND v.deleted = false
                ORDER BY v.displayOrder ASC, v.createdAt DESC
            """)
    Page<MenuItemVariant> findByMenuItemIdAndNotDeleted(
            @Param("menuItemId") UUID menuItemId,
            Pageable pageable
    );

    @Query("""
                SELECT COUNT(v) > 0 FROM MenuItemVariant v
                WHERE v.menuItem.id = :menuItemId
                AND LOWER(v.nameEn) = LOWER(:nameEn)
                AND v.deleted = false
            """)
    boolean existsByMenuItemIdAndNameEn(
            @Param("menuItemId") UUID menuItemId,
            @Param("nameEn") String nameEn
    );

    @Query("""
                SELECT COUNT(v) > 0 FROM MenuItemVariant v
                WHERE v.menuItem.id = :menuItemId
                AND LOWER(v.nameEn) = LOWER(:nameEn)
                AND v.id <> :variantId
                AND v.deleted = false
            """)
    boolean existsByMenuItemIdAndNameEnAndIdNot(
            @Param("menuItemId") UUID menuItemId,
            @Param("nameEn") String nameEn,
            @Param("variantId") UUID variantId
    );

    @Modifying
    @Query("""
                UPDATE MenuItemVariant v
                SET v.defaultVariant = false
                WHERE v.menuItem.id = :menuItemId
                AND v.id <> :currentVariantId
                AND v.deleted = false
            """)
    void unsetOtherDefaultVariants(
            @Param("menuItemId") UUID menuItemId,
            @Param("currentVariantId") UUID currentVariantId
    );

    @Query("""
                SELECT COUNT(v) > 0 FROM MenuItemVariant v
                WHERE v.menuItem.id = :menuItemId
                AND v.displayOrder = :displayOrder
                AND v.deleted = false
            """)
    boolean existsByMenuItemIdAndDisplayOrder(
            @Param("menuItemId") UUID menuItemId,
            @Param("displayOrder") Integer displayOrder
    );

    @Query("""
                SELECT COUNT(v) > 0 FROM MenuItemVariant v
                WHERE v.menuItem.id = :menuItemId
                AND v.displayOrder = :displayOrder
                AND v.id <> :variantId
                AND v.deleted = false
            """)
    boolean existsByMenuItemIdAndDisplayOrderAndIdNot(
            @Param("menuItemId") UUID menuItemId,
            @Param("displayOrder") Integer displayOrder,
            @Param("variantId") UUID variantId
    );
}