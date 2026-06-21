package co.istad.smartserve.features.category.factory;

import co.istad.smartserve.features.category.Category;
import co.istad.smartserve.features.restaurant.Restaurant;
import org.springframework.stereotype.Component;

@Component
public class CategoryFactory {
    /*
    * Factory Method Pattern used to give interface can create object
    * and allow subclass can defined object that they want to create
    * and this class can create object instead of Category service layer
    */
    public Category create(
            Restaurant restaurant,
            String nameEn,
            String nameKh,
            String iconUrl,
            Integer displayOrder,
            Boolean status
    ) {
        return Category.builder()
                .restaurant(restaurant)
                .nameEn(nameEn)
                .nameKh(nameKh)
                .iconUrl(iconUrl)
                .displayOrder(displayOrder)
                .status(status == null || status)
                .build();
    }
}
