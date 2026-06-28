package co.istad.smartserve.features.qrcode.factory;

import co.istad.smartserve.features.qrcode.QrCode;
import co.istad.smartserve.features.qrcode.QrCodeType;
import co.istad.smartserve.features.restaurant.Restaurant;
import co.istad.smartserve.features.table.RestaurantTable;
import org.springframework.stereotype.Component;

@Component
public class DefaultQrCodeFactory implements QrCodeFactory {
    @Override
    public QrCode createPublicMenuQr(
            Restaurant restaurant,
            String qrValue,
            String qrUrl
    ) {
        return QrCode.builder()
                .restaurant(restaurant)
                .restaurantTable(null)
                .qrValue(qrValue)
                .qrUrl(qrUrl)
                .qrCodeType(QrCodeType.PUBLIC_MENU)
                .status(true)
                .build();
    }

    @Override
    public QrCode createTableQr(
            Restaurant restaurant,
            RestaurantTable restaurantTable,
            String qrValue,
            String qrUrl
    ) {
        return QrCode.builder()
                .restaurant(restaurant)
                .restaurantTable(restaurantTable)
                .qrValue(qrValue)
                .qrUrl(qrUrl)
                .qrCodeType(QrCodeType.TABLE_QR)
                .status(true)
                .build();
    }
}
