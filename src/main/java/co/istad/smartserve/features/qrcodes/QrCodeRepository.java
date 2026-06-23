package co.istad.smartserve.features.qrcodes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QrCodeRepository extends JpaRepository<QrCode, Long> {
    @Query("SELECT q FROM QrCode q WHERE q.restaurant.id = :restaurantId " +
            "AND (:type IS NULL OR q.qrType = :type) " +
            "AND (:status IS NULL OR q.status = :status)")
    Page<QrCode> findByRestaurantIdFiltered(
            @Param("restaurantId") UUID restaurantId,
            @Param("type") String type,
            @Param("status") Boolean status,
            Pageable pageable);

    Optional<QrCode> findByQrValue(String qrValue);
}
