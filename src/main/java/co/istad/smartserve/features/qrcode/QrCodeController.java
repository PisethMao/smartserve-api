package co.istad.smartserve.features.qrcode;

import co.istad.smartserve.features.qrcode.dto.CreateQrCodeRequest;
import co.istad.smartserve.features.qrcode.dto.QrCodeResponse;
import co.istad.smartserve.features.qrcode.dto.UpdateQrCodeStatusRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "QR Codes", description = "Manage restaurant public menu QR and table QR codes")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class QrCodeController {
    private final QrCodeService qrCodeService;

    /**
     * Generate a new QR code for a restaurant.
     *
     * @param request QR code generation request
     * @return generated QR code response
     */
    @Operation(summary = "Create QR code for restaurant")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/restaurants/{restaurantId}/qr-codes")
    public QrCodeResponse createQrCode(
            @PathVariable UUID restaurantId,
            @Valid @RequestBody CreateQrCodeRequest request
    ) {
        return qrCodeService.createQrCode(restaurantId, request);
    }

    /**
     * Retrieve QR codes for a restaurant with filtering and pagination.
     *
     * @param restaurantId the restaurant ID
     * @param pageable     pagination and sorting information
     * @return page of QR codes for the restaurant
     */
    @Operation(summary = "Get QR codes by restaurant")
    @GetMapping("/restaurants/{restaurantId}/qr-codes")
    public Page<QrCodeResponse> getQrCodesByRestaurant(
            @PathVariable UUID restaurantId,
            @ParameterObject
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return qrCodeService.getQrCodesByRestaurant(restaurantId, pageable);
    }

    /**
     * Retrieve a single QR code by ID.
     *
     * @return QR code response
     */
    @Operation(summary = "Get QR code by ID")
    @GetMapping("/qr-codes/{qrCodeId}")
    public QrCodeResponse getQrCodeById(
            @PathVariable UUID qrCodeId
    ) {
        return qrCodeService.getQrCodeById(qrCodeId);
    }

    @Operation(summary = "Get QR code by QR value")
    @GetMapping("/public/qr-codes/{qrValue}")
    public QrCodeResponse getQrCodeByValue(
            @PathVariable String qrValue
    ) {
        return qrCodeService.getQrCodeByValue(qrValue);
    }

    /**
     * Update an existing QR code.
     *
     * @param request update request data
     * @return updated QR code response
     */
    @Operation(summary = "Update QR code status")
    @PatchMapping("/qr-codes/{qrCodeId}/status")
    public QrCodeResponse updateQrCodeStatus(
            @PathVariable UUID qrCodeId,
            @Valid @RequestBody UpdateQrCodeStatusRequest request
    ) {
        return qrCodeService.updateQrCodeStatus(qrCodeId, request);
    }

    /**
     * Delete a QR code by ID.
     *
     */
    @Operation(summary = "Delete QR code")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/qr-codes/{qrCodeId}")
    public void deleteQrCode(
            @PathVariable UUID qrCodeId
    ) {
        qrCodeService.deleteQrCode(qrCodeId);
    }
}
