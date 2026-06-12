package co.istad.smartserve.features.restaurant;

import co.istad.smartserve.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "restaurants")
public class Restaurant extends BaseEntity {
    // Inheritance Concept
    @Column(name = "name_en", nullable = false, length = 150)
    private String nameEn;
    @Column(name = "name_kh", length = 150)
    private String nameKh;
    @Column(name = "description_en", columnDefinition = "TEXT")
    private String descriptionEn;
    @Column(name = "description_kh", columnDefinition = "TEXT")
    private String descriptionKh;
    @Column(name = "address", length = 255)
    private String address;
    @Column(length = 30)
    private String phone;
    @Column(length = 150)
    private String email;
    @Column(length = 500, name = "logo_url")
    private String logoUrl;
    @Builder.Default
    /*
     * Builder Pattern use for set object that we want
     * and don't set that we don't want.
     */
    @Column(name = "status", nullable = false)
    private boolean active = true;
}
