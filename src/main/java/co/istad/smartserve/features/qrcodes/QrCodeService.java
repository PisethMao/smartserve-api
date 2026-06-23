package co.istad.smartserve.features.qrcodes;

import co.istad.smartserve.features.qrcodes.dto.QrCodeRequest;
import co.istad.smartserve.features.qrcodes.dto.QrCodeResponse;
import co.istad.smartserve.features.qrcodes.dto.QrCodeUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface QrCodeService {
    QrCodeResponse generateQrCode(QrCodeRequest request);
    void deactivateQrCode(Long qrCodeId);
    Page<QrCodeResponse> getQrCodesByRestaurant(UUID restaurantId, String type, Boolean status, Pageable pageable);
    QrCodeResponse getQrCodeById(Long id);
    QrCodeResponse updateQrCode(Long id, QrCodeUpdateRequest request);
    void deleteQrCode(Long id);
}
