package co.istad.smartserve.features.qrcodes;

import co.istad.smartserve.features.qrcodes.dto.QrCodeRequest;
import co.istad.smartserve.features.qrcodes.dto.QrCodeResponse;
import co.istad.smartserve.features.qrcodes.dto.QrCodeUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for QR Code operations.
 * Requires RESTAURANT_ADMIN or SUPER_ADMIN roles for all operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/qrcodes")
@RequiredArgsConstructor
@Tag(name = "QR Code", description = "QR code management RESTful APIs")
public class QrCodeController {

    private final QrCodeService qrCodeService;

    /**
     * Generate a new QR code for a restaurant.
     * 
     * @param request QR code generation request
     * @return generated QR code response
     */
    @PostMapping
    @Operation(summary = "Generate a new QR code")
    @PreAuthorize("hasAnyRole('RESTAURANT_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<QrCodeResponse> generateQrCode(@Valid @RequestBody QrCodeRequest request) {
        log.info("API call to generate QR code for restaurant: {}", request.restaurantId());
        QrCodeResponse response = qrCodeService.generateQrCode(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Deactivate a QR code by ID.
     * 
     * @param id the QR code ID to deactivate
     */
    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate a QR code")
    @PreAuthorize("hasAnyRole('RESTAURANT_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Void> deactivateQrCode(@PathVariable Long id) {
        log.info("API call to deactivate QR code: {}", id);
        qrCodeService.deactivateQrCode(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieve QR codes for a restaurant with filtering and pagination.
     * 
     * @param restaurantId the restaurant ID
     * @param type optional QR type filter
     * @param status optional status filter
     * @param pageable pagination and sorting information
     * @return page of QR codes for the restaurant
     */
    @GetMapping("/restaurant/{restaurantId}")
    @Operation(summary = "Get QR codes by restaurant with filtering and pagination")
    @PreAuthorize("hasAnyRole('RESTAURANT_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Page<QrCodeResponse>> getQrCodesByRestaurant(
            @PathVariable UUID restaurantId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Boolean status,
            @ParameterObject @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.debug("API call to get QR codes for restaurant: {}, type: {}, status: {}", 
                restaurantId, type, status);
        Page<QrCodeResponse> responses = qrCodeService.getQrCodesByRestaurant(restaurantId, type, status, pageable);
        return ResponseEntity.ok(responses);
    }

    /**
     * Retrieve a single QR code by ID.
     * 
     * @param id the QR code ID
     * @return QR code response
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get QR code by ID")
    @PreAuthorize("hasAnyRole('RESTAURANT_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<QrCodeResponse> getQrCodeById(@PathVariable Long id) {
        log.debug("API call to get QR code: {}", id);
        QrCodeResponse response = qrCodeService.getQrCodeById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Update an existing QR code.
     * 
     * @param id the QR code ID
     * @param request update request data
     * @return updated QR code response
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing QR code")
    @PreAuthorize("hasAnyRole('RESTAURANT_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<QrCodeResponse> updateQrCode(
            @PathVariable Long id,
            @Valid @RequestBody QrCodeUpdateRequest request) {
        log.info("API call to update QR code: {}", id);
        QrCodeResponse response = qrCodeService.updateQrCode(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a QR code by ID.
     * 
     * @param id the QR code ID to delete
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a QR code")
    @PreAuthorize("hasAnyRole('RESTAURANT_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Void> deleteQrCode(@PathVariable Long id) {
        log.info("API call to delete QR code: {}", id);
        qrCodeService.deleteQrCode(id);
        return ResponseEntity.noContent().build();
    }
}
