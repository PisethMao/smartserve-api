package co.istad.smartserve.features.qrcode;

import co.istad.smartserve.features.qrcode.dto.CreateQrCodeRequest;
import co.istad.smartserve.features.qrcode.dto.QrCodeResponse;
import co.istad.smartserve.features.qrcode.dto.UpdateQrCodeStatusRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface QrCodeService {
    QrCodeResponse createQrCode(
            UUID restaurantId,
            CreateQrCodeRequest request
    );

    Page<QrCodeResponse> getQrCodesByRestaurant(
            UUID restaurantId,
            Pageable pageable
    );

    QrCodeResponse getQrCodeById(UUID qrCodeId);

    QrCodeResponse getQrCodeByValue(String qrValue);

    QrCodeResponse updateQrCodeStatus(
            UUID qrCodeId,
            UpdateQrCodeStatusRequest request
    );

    void deleteQrCode(UUID qrCodeId);
}
