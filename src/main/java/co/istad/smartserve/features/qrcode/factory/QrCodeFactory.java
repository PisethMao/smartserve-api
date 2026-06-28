package co.istad.smartserve.features.qrcode.factory;

import co.istad.smartserve.features.qrcode.QrCode;
import co.istad.smartserve.features.restaurant.Restaurant;
import co.istad.smartserve.features.table.RestaurantTable;

public interface QrCodeFactory {
    QrCode createPublicMenuQr(
            Restaurant restaurant,
            String qrValue,
            String qrUrl
    );
    QrCode createTableQr(
            Restaurant restaurant,
            RestaurantTable restaurantTable,
            String qrValue,
            String qrUrl
    );
}
