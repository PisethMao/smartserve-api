package co.istad.smartserve.features.qrcodes;

import co.istad.smartserve.features.qrcodes.dto.QrCodeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QrCodeMapper {

    @Mapping(target = "restaurantId", source = "restaurant.id")
    QrCodeResponse toResponse(QrCode qrCode);

    List<QrCodeResponse> toResponseList(List<QrCode> qrCodes);

    default Page<QrCodeResponse> toResponsePage(Page<QrCode> qrCodePage) {
        return qrCodePage.map(this::toResponse);
    }
}
