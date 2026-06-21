package co.istad.smartserve.features.category.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CategoryChangedEventListener {
    /*
    * We use the Observer Pattern because the category service can publish
    * a category-changed event, and listeners can observe and react to
    * that event without the service directly depending on them.
    */
    @EventListener
    public void onCategoryChanged(CategoryChangedEvent categoryChangedEvent) {
        log.info("Category changed, restaurantId={}, categoryId={}, action={}",
                categoryChangedEvent.restaurantId(), categoryChangedEvent.categoryId(), categoryChangedEvent.action());
    }
}
