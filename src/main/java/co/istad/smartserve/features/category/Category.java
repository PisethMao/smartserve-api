package co.istad.smartserve.features.category;

import co.istad.smartserve.common.domain.BaseEntity;
import co.istad.smartserve.features.restaurant.Restaurant;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
    @Column(nullable = false, length = 100, name = "name_en")
    private String nameEn;
    @Column(length = 100, name = "name_kh")
    private String nameKh;
    @Column(name = "icon_url")
    private String iconUrl;
    @Column(name = "display_order")
    private Integer displayOrder;
    @Builder.Default
    @Column(name = "status")
    private Boolean status = true;

    public void update(String nameEn, String nameKh, String iconUrl, Integer displayOrder, Boolean status) {
        this.nameEn = nameEn;
        this.nameKh = nameKh;
        this.iconUrl = iconUrl;
        this.displayOrder = displayOrder;
        this.status = status;
    }

    public void disable() {
        this.status = false;
    }

    public void enable() {
        this.status = true;
    }
}

