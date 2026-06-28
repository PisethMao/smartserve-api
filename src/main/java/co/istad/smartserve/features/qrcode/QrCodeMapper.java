package co.istad.smartserve.features.qrcode;

import co.istad.smartserve.features.qrcode.dto.QrCodeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface QrCodeMapper {
    @Mapping(source = "restaurant.id", target = "restaurantId")
    @Mapping(source = "restaurantTable.id", target = "tableId")
    QrCodeResponse toResponse(QrCode qrCode);
}
