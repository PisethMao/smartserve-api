package co.istad.smartserve.features.menuitems.variant.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MenuItemVariantChangedEventListener {
    @EventListener
    public void onMenuItemVariantChangedEvent(MenuItemVariantChangedEvent event) {
        log.info(
                "Menu Item Variant Changed!, menuItemId={}, variantId={}, action={}",
                event.menuItemId(), event.variantId(), event.action()
        );
    }
}
