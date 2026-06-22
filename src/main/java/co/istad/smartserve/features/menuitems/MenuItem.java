package co.istad.smartserve.features.menuitems;

import co.istad.smartserve.common.domain.BaseEntity;
import co.istad.smartserve.features.category.Category;
import co.istad.smartserve.features.restaurant.Restaurant;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "menu_items")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuItem extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @Column(name = "name_en", nullable = false, length = 150)
    private String nameEn;
    @Column(name = "name_kh", length = 150)
    private String nameKh;
    @Column(name = "description_en", columnDefinition = "TEXT")
    private String descriptionEn;
    @Column(name = "description_kh", columnDefinition = "TEXT")
    private String descriptionKh;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;
    @Column(name = "image_url", length = 500)
    private String imageUrl;
    @Column(name = "sold_limit")
    private Integer soldLimit;
    @Enumerated(EnumType.STRING)
    @Column(name = "availability_status", nullable = false, length = 30)
    private MenuItemAvailabilityStatus menuItemAvailabilityStatus;
    @Column(nullable = false)
    private Boolean status;

    @Builder
    public MenuItem(
            Restaurant restaurant,
            Category category,
            String nameEn,
            String nameKh,
            String descriptionEn,
            String descriptionKh,
            BigDecimal price,
            String imageUrl,
            Integer soldLimit,
            MenuItemAvailabilityStatus menuItemAvailabilityStatus,
            Boolean status
    ) {
        this.restaurant = restaurant;
        this.category = category;
        this.nameEn = nameEn;
        this.nameKh = nameKh;
        this.descriptionEn = descriptionEn;
        this.descriptionKh = descriptionKh;
        this.price = price;
        this.imageUrl = imageUrl;
        this.soldLimit = soldLimit;
        this.menuItemAvailabilityStatus = menuItemAvailabilityStatus;
        this.status = status;
    }
}
