package co.istad.smartserve.features.menuitems.variant.command;

import org.springframework.stereotype.Component;

@Component
public class MenuItemVariantCommandInvoker {
    public <R> R invoke(MenuItemVariantCommand<R> command) {
        return command.execute();
    }
}
