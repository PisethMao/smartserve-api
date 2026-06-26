package co.istad.smartserve.features.table.pattern;

import org.springframework.stereotype.Component;

@Component
public class RestaurantTableCommandInvoker {
    public <T> T execute(RestaurantTableCommand<T> command) {
        return command.execute();
    }
}
