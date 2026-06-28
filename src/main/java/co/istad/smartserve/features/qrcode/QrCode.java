package co.istad.smartserve.features.qrcode;

import co.istad.smartserve.common.domain.BaseEntity;
import co.istad.smartserve.features.restaurant.Restaurant;
import co.istad.smartserve.features.table.RestaurantTable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "qr_codes",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_qr_codes_qr_value",
                        columnNames = "qr_value"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QrCode extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id")
    private RestaurantTable restaurantTable;
    @Column(name = "qr_value", unique = true, nullable = false, length = 100)
    private String qrValue;
    @Column(name = "qr_url", nullable = false, length = 500)
    private String qrUrl;
    @Enumerated(EnumType.STRING)
    @Column(name = "qr_type", nullable = false, length = 30)
    private QrCodeType qrCodeType;
    @Builder.Default
    @Column(name = "status", nullable = false)
    private Boolean status = true;
}
