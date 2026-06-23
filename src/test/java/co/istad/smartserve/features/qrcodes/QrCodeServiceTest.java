package co.istad.smartserve.features.qrcodes;

import co.istad.smartserve.features.qrcodes.command.DeactivateQrCodeCommand;
import co.istad.smartserve.features.qrcodes.command.GenerateQrCodeCommand;
import co.istad.smartserve.features.qrcodes.composite.QrCodeAbstraction;
import co.istad.smartserve.features.qrcodes.dto.QrCodeRequest;
import co.istad.smartserve.features.qrcodes.dto.QrCodeResponse;
import co.istad.smartserve.features.qrcodes.dto.QrCodeUpdateRequest;
import co.istad.smartserve.features.qrcodes.factory.QrCodeFactory;
import co.istad.smartserve.features.qrcodes.factory.QrStrategyPayload;
import co.istad.smartserve.features.restaurant.Restaurant;
import co.istad.smartserve.features.restaurant.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QrCodeServiceTest {

    @Mock private QrCodeRepository qrCodeRepository;
    @Mock private RestaurantRepository restaurantRepository;
    @Mock private QrCodeMapper qrCodeMapper;
    @Mock private QrCodeFactory qrCodeFactory;
    @Mock private QrGeneratorAdapter qrGeneratorAdapter;
    @Mock private QrCodeAbstraction qrCodeAbstraction;
    @Mock private ApplicationEventPublisher eventPublisher;
    @Mock private Authentication authentication;
    @Mock private SecurityContext securityContext;

    private QrCodeServiceImpl qrCodeService;

    @BeforeEach
    void setUp() {
        qrCodeService = new QrCodeServiceImpl(
                qrCodeRepository,
                restaurantRepository,
                qrCodeMapper,
                qrCodeFactory,
                qrGeneratorAdapter,
                qrCodeAbstraction,
                eventPublisher
        );
        SecurityContextHolder.setContext(securityContext);
    }

    private void mockAuthentication(UUID restaurantId) {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaimAsString("role")).thenReturn("ROLE_RESTAURANT_ADMIN");
        when(jwt.getClaimAsString("restaurant_id")).thenReturn(restaurantId.toString());
        when(authentication.getPrincipal()).thenReturn(jwt);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    void testGenerateQrCode_Success() throws IOException {
        UUID restaurantId = UUID.randomUUID();
        mockAuthentication(restaurantId);
        QrCodeRequest request = QrCodeRequest.builder()
                .restaurantId(restaurantId)
                .qrType("PUBLIC_MENU")
                .qrUrl("http://example.com")
                .qrValue("REST-001")
                .status(true)
                .build();

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        QrStrategyPayload payload = QrStrategyPayload.builder()
                .content("http://example.com/menu")
                .type("PUBLIC_MENU")
                .build();

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(qrCodeFactory.createPayload(any(), any(), any())).thenReturn(payload);
        when(qrGeneratorAdapter.generateQrCodeImage(any(), anyInt(), anyInt())).thenReturn(new byte[]{1, 2, 3});
        when(qrCodeAbstraction.produce(any(), any())).thenReturn("rendered-url");
        when(qrCodeRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(qrCodeMapper.toResponse(any())).thenReturn(QrCodeResponse.builder().qrUrl("rendered-url").build());

        QrCodeResponse response = qrCodeService.generateQrCode(request);

        assertNotNull(response);
        assertEquals("rendered-url", response.qrUrl());
        verify(qrCodeRepository).save(any());
    }

    @Test
    void testDeactivateQrCode_Success() {
        Long qrId = 1L;
        UUID restaurantId = UUID.randomUUID();
        mockAuthentication(restaurantId);
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        
        QrCode qrCode = new QrCode();
        qrCode.setId(qrId);
        qrCode.setStatus(true);
        qrCode.setRestaurant(restaurant);

        when(qrCodeRepository.findById(qrId)).thenReturn(Optional.of(qrCode));

        qrCodeService.deactivateQrCode(qrId);

        assertFalse(qrCode.getStatus());
        verify(qrCodeRepository).save(qrCode);
        verify(eventPublisher).publishEvent(any());
    }

    @Test
    void testDeactivateQrCode_NotFound() {
        Long qrId = 1L;
        when(qrCodeRepository.findById(qrId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> qrCodeService.deactivateQrCode(qrId));
    }

    @Test
    void testGetQrCodesByRestaurant_Success() {
        UUID restaurantId = UUID.randomUUID();
        mockAuthentication(restaurantId);
        Pageable pageable = PageRequest.of(0, 10);
        
        QrCode qrCode = new QrCode();
        Page<QrCode> qrCodePage = new PageImpl<>(java.util.List.of(qrCode));
        
        when(qrCodeRepository.findByRestaurantIdFiltered(eq(restaurantId), any(), any(), eq(pageable)))
                .thenReturn(qrCodePage);
        when(qrCodeMapper.toResponsePage(any())).thenReturn(new PageImpl<>(java.util.List.of(QrCodeResponse.builder().build())));

        Page<QrCodeResponse> result = qrCodeService.getQrCodesByRestaurant(restaurantId, null, null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(qrCodeRepository).findByRestaurantIdFiltered(eq(restaurantId), any(), any(), eq(pageable));
    }

    @Test
    void testUpdateQrCode_Success() throws IOException {
        Long qrId = 1L;
        UUID restaurantId = UUID.randomUUID();
        mockAuthentication(restaurantId);
        
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        
        QrCode qrCode = new QrCode();
        qrCode.setId(qrId);
        qrCode.setRestaurant(restaurant);
        qrCode.setQrType("PUBLIC_MENU");
        qrCode.setQrUrl("old-url");
        qrCode.setQrValue("OLD-VALUE");

        QrCodeUpdateRequest request = QrCodeUpdateRequest.builder()
                .qrType("TABLE_QR")
                .tableId(5)
                .qrValue("NEW-VALUE")
                .build();

        QrStrategyPayload payload = QrStrategyPayload.builder()
                .content("http://example.com/table/5")
                .type("TABLE_QR")
                .tableId(5)
                .build();

        when(qrCodeRepository.findById(qrId)).thenReturn(Optional.of(qrCode));
        when(qrCodeFactory.createPayload(any(), any(), any())).thenReturn(payload);
        when(qrGeneratorAdapter.generateQrCodeImage(any(), anyInt(), anyInt())).thenReturn(new byte[]{4, 5, 6});
        when(qrCodeAbstraction.produce(any(), any())).thenReturn("new-rendered-url");
        when(qrCodeRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(qrCodeMapper.toResponse(any())).thenReturn(QrCodeResponse.builder().qrUrl("new-rendered-url").build());

        QrCodeResponse response = qrCodeService.updateQrCode(qrId, request);

        assertNotNull(response);
        verify(qrCodeRepository).save(qrCode);
        assertEquals("TABLE_QR", qrCode.getQrType());
        assertEquals(5, qrCode.getTableId());
        assertEquals("NEW-VALUE", qrCode.getQrValue());
    }

    @Test
    void testDeleteQrCode_Success() {
        Long qrId = 1L;
        UUID restaurantId = UUID.randomUUID();
        mockAuthentication(restaurantId);
        
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        
        QrCode qrCode = new QrCode();
        qrCode.setId(qrId);
        qrCode.setRestaurant(restaurant);

        when(qrCodeRepository.findById(qrId)).thenReturn(Optional.of(qrCode));

        qrCodeService.deleteQrCode(qrId);

        verify(qrCodeRepository).delete(qrCode);
    }
}
