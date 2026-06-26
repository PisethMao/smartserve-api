package co.istad.smartserve.features.table.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RestaurantTableChangedEventListener {
    @EventListener
    public void onRestaurantTableChanged(RestaurantTableChangedEvent event) {
        log.info(
                "RESTAURANT TABLE EVENT => action={}, tableId={}, restaurantId={}",
                event.action(),
                event.tableId(),
                event.restaurantId()
        );
    }
}
