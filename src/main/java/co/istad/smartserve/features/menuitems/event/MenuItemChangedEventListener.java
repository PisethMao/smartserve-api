package co.istad.smartserve.features.menuitems.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MenuItemChangedEventListener {
    @EventListener
    public void onMenuItemChangedEvent(MenuItemChangedEvent event) {
        log.info("Menu Item Changed: restaurantId={}, categoryId={}, menuItemId={}, action={}",
                event.restaurantId(), event.categoryId(), event.menuItemId(), event.action());
    }
}
