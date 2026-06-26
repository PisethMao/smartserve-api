package co.istad.smartserve.features.table.pattern;

import co.istad.smartserve.features.restaurant.Restaurant;
import co.istad.smartserve.features.table.RestaurantTable;
import co.istad.smartserve.features.table.TableStatus;

public class RestaurantTableBuilder {
    private Restaurant restaurant;
    private String tableNumber;
    private Integer capacity;
    private TableStatus tableStatus = TableStatus.AVAILABLE;
    private String floorName;
    private String zoneName;
    private Boolean status = true;

    public static RestaurantTableBuilder create() {
        return new RestaurantTableBuilder();
    }

    public RestaurantTableBuilder restaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        return this;
    }

    public RestaurantTableBuilder tableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
        return this;
    }

    public RestaurantTableBuilder capacity(Integer capacity) {
        this.capacity = capacity;
        return this;
    }

    public RestaurantTableBuilder tableStatus(TableStatus tableStatus) {
        if (tableStatus != null) {
            this.tableStatus = tableStatus;
        }
        return this;
    }

    public RestaurantTableBuilder floorName(String floorName) {
        this.floorName = floorName;
        return this;
    }

    public RestaurantTableBuilder zoneName(String zoneName) {
        this.zoneName = zoneName;
        return this;
    }

    public RestaurantTableBuilder status(Boolean status) {
        if (status != null) {
            this.status = status;
        }
        return this;
    }

    public RestaurantTable build() {
        return RestaurantTable.builder()
                .restaurant(restaurant)
                .tableNumber(tableNumber.trim())
                .capacity(capacity)
                .tableStatus(tableStatus)
                .floorName(floorName)
                .zoneName(zoneName)
                .status(status)
                .build();
    }
}
