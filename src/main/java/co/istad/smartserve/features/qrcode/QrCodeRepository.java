package co.istad.smartserve.features.qrcode;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QrCodeRepository extends JpaRepository<QrCode, UUID> {
    @Query("""
           SELECT q FROM QrCode q
           JOIN FETCH q.restaurant r
           LEFT JOIN FETCH q.restaurantTable t
           WHERE q.id = :id
           AND q.deleted = false
           """)
    Optional<QrCode> findByIdAndDeletedFalse(UUID id);

    @Query("""
           SELECT q FROM QrCode q
           JOIN FETCH q.restaurant r
           LEFT JOIN FETCH q.restaurantTable t
           WHERE q.qrValue = :qrValue
           AND q.deleted = false
           """)
    Optional<QrCode> findByQrValueAndDeletedFalse(String qrValue);

    @Query(
            value = """
                    SELECT q FROM QrCode q
                    JOIN FETCH q.restaurant r
                    LEFT JOIN FETCH q.restaurantTable t
                    WHERE r.id = :restaurantId
                    AND q.deleted = false
                    ORDER BY q.createdAt DESC
                    """,
            countQuery = """
                    SELECT COUNT(q) FROM QrCode q
                    WHERE q.restaurant.id = :restaurantId
                    AND q.deleted = false
                    """
    )
    Page<QrCode> findByRestaurantIdAndDeletedFalse(UUID restaurantId, Pageable pageable);

    boolean existsByQrValueAndDeletedFalse(String qrValue);

    boolean existsByRestaurant_IdAndRestaurantTable_IdAndDeletedFalse(
            UUID restaurantId,
            UUID tableId
    );

    boolean existsByRestaurant_IdAndQrCodeTypeAndDeletedFalse(
            UUID restaurantId,
            QrCodeType qrType
    );
}
