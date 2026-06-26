package co.istad.smartserve.features.table;

import co.istad.smartserve.common.domain.BaseEntity;
import co.istad.smartserve.features.restaurant.Restaurant;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "restaurant_tables")
public class RestaurantTable extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
    @Column(name = "table_number", nullable = false, length = 50)
    private String tableNumber;
    @Column(name = "capacity")
    private Integer capacity;
    @Enumerated(EnumType.STRING)
    @Column(name = "table_status", nullable = false, length = 30)
    private TableStatus tableStatus;
    @Column(name = "floor_name", length = 100)
    private String floorName;
    @Column(name = "zone_name", length = 100)
    private String zoneName;
    @Column(nullable = false)
    private Boolean status;
}
