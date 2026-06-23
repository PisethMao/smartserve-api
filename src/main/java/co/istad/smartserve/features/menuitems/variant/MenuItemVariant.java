package co.istad.smartserve.features.menuitems.variant;

import co.istad.smartserve.common.domain.BaseEntity;
import co.istad.smartserve.features.menuitems.MenuItem;
import co.istad.smartserve.features.menuitems.MenuItemAvailabilityStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "menu_item_variants")
@Getter
@Setter
@NoArgsConstructor
public class MenuItemVariant extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;
    @Column(name = "name_en", nullable = false, length = 100)
    private String nameEn;
    @Column(name = "name_kh", length = 100)
    private String nameKh;
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    @Column(name = "display_order", nullable = false)
    private Integer displayOrder = 0;
    @Enumerated(EnumType.STRING)
    @Column(name = "availability_status", nullable = false, length = 30)
    private MenuItemAvailabilityStatus availabilityStatus = MenuItemAvailabilityStatus.AVAILABLE;
    @Column(name = "is_default", nullable = false)
    private Boolean defaultVariant = false;
}
