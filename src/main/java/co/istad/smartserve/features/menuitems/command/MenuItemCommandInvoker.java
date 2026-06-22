package co.istad.smartserve.features.menuitems.command;

import co.istad.smartserve.features.menuitems.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MenuItemCommandInvoker {
    private final MenuItemService menuItemService;

    public <R> R execute(MenuItemCommand<R> command) {
        return command.execute(menuItemService);
    }
}
