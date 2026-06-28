package co.istad.smartserve.features.qrcode;

import co.istad.smartserve.exception.ConflictException;
import co.istad.smartserve.exception.ResourceNotFoundException;
import co.istad.smartserve.features.qrcode.command.QrCodeCommandInvoker;
import co.istad.smartserve.features.qrcode.dto.CreateQrCodeRequest;
import co.istad.smartserve.features.qrcode.dto.QrCodeResponse;
import co.istad.smartserve.features.qrcode.dto.UpdateQrCodeStatusRequest;
import co.istad.smartserve.features.qrcode.event.QrCodeChangedEvent;
import co.istad.smartserve.features.qrcode.factory.QrCodeFactory;
import co.istad.smartserve.features.restaurant.Restaurant;
import co.istad.smartserve.features.restaurant.RestaurantRepository;
import co.istad.smartserve.features.table.RestaurantTable;
import co.istad.smartserve.features.table.RestaurantTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QrCodeServiceTest {

    @Mock
    private QrCodeRepository qrCodeRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RestaurantTableRepository restaurantTableRepository;

    @Mock
    private QrCodeMapper qrCodeMapper;

    @Mock
    private QrGeneratorAdapter qrGeneratorAdapter;

    @Mock
    private QrCodeFactory qrCodeFactory;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private QrCodeServiceImpl qrCodeService;

    @BeforeEach
    void setUp() {
        QrCodeCommandInvoker qrCodeCommandInvoker = new QrCodeCommandInvoker();

        qrCodeService = new QrCodeServiceImpl(
                qrCodeRepository,
                restaurantRepository,
                restaurantTableRepository,
                qrCodeMapper,
                qrGeneratorAdapter,
                qrCodeFactory,
                eventPublisher,
                qrCodeCommandInvoker
        );
    }

    @Test
    void testCreatePublicMenuQrCode_Success() {
        UUID restaurantId = UUID.randomUUID();
        UUID qrCodeId = UUID.randomUUID();

        CreateQrCodeRequest request = CreateQrCodeRequest.builder()
                .qrCodeType(QrCodeType.PUBLIC_MENU)
                .build();

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        QrCode qrCode = new QrCode();
        qrCode.setId(qrCodeId);
        qrCode.setRestaurant(restaurant);
        qrCode.setQrCodeType(QrCodeType.PUBLIC_MENU);
        qrCode.setQrValue("QR-001");
        qrCode.setQrUrl("http://localhost:3000/menu/QR-001");
        qrCode.setStatus(true);
        qrCode.setDeleted(false);

        QrCodeResponse expectedResponse = QrCodeResponse.builder()
                .id(qrCodeId)
                .restaurantId(restaurantId)
                .qrCodeType(QrCodeType.PUBLIC_MENU)
                .qrValue("QR-001")
                .qrUrl("http://localhost:3000/menu/QR-001")
                .status(true)
                .build();

        when(restaurantRepository.findByIdAndDeletedFalse(restaurantId))
                .thenReturn(Optional.of(restaurant));

        when(qrCodeRepository.existsByRestaurant_IdAndQrCodeTypeAndDeletedFalse(
                restaurantId,
                QrCodeType.PUBLIC_MENU
        )).thenReturn(false);

        when(qrGeneratorAdapter.generateQrValue())
                .thenReturn("QR-001");

        when(qrCodeRepository.existsByQrValueAndDeletedFalse("QR-001"))
                .thenReturn(false);

        when(qrGeneratorAdapter.generatePublicMenuUrl("QR-001"))
                .thenReturn("http://localhost:3000/menu/QR-001");

        when(qrCodeFactory.createPublicMenuQr(
                restaurant,
                "QR-001",
                "http://localhost:3000/menu/QR-001"
        )).thenReturn(qrCode);

        when(qrCodeRepository.save(qrCode))
                .thenReturn(qrCode);

        when(qrCodeMapper.toResponse(qrCode))
                .thenReturn(expectedResponse);

        QrCodeResponse response = qrCodeService.createQrCode(restaurantId, request);

        assertNotNull(response);
        assertEquals(qrCodeId, response.id());
        assertEquals(restaurantId, response.restaurantId());
        assertEquals(QrCodeType.PUBLIC_MENU, response.qrCodeType());
        assertEquals("QR-001", response.qrValue());
        assertEquals("http://localhost:3000/menu/QR-001", response.qrUrl());
        assertTrue(response.status());

        verify(qrCodeRepository).save(qrCode);
        verify(eventPublisher).publishEvent(any(QrCodeChangedEvent.class));
        verifyNoInteractions(restaurantTableRepository);
    }

    @Test
    void testCreatePublicMenuQrCode_AlreadyExists() {
        UUID restaurantId = UUID.randomUUID();

        CreateQrCodeRequest request = CreateQrCodeRequest.builder()
                .qrCodeType(QrCodeType.PUBLIC_MENU)
                .build();

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        when(restaurantRepository.findByIdAndDeletedFalse(restaurantId))
                .thenReturn(Optional.of(restaurant));

        when(qrCodeRepository.existsByRestaurant_IdAndQrCodeTypeAndDeletedFalse(
                restaurantId,
                QrCodeType.PUBLIC_MENU
        )).thenReturn(true);

        assertThrows(
                ConflictException.class,
                () -> qrCodeService.createQrCode(restaurantId, request)
        );

        verify(qrCodeRepository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void testCreateTableQrCode_Success() {
        UUID restaurantId = UUID.randomUUID();
        UUID tableId = UUID.randomUUID();
        UUID qrCodeId = UUID.randomUUID();

        CreateQrCodeRequest request = CreateQrCodeRequest.builder()
                .qrCodeType(QrCodeType.TABLE_QR)
                .tableId(tableId)
                .build();

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        RestaurantTable table = new RestaurantTable();
        table.setId(tableId);
        table.setRestaurant(restaurant);

        QrCode qrCode = new QrCode();
        qrCode.setId(qrCodeId);
        qrCode.setRestaurant(restaurant);
        qrCode.setRestaurantTable(table);
        qrCode.setQrCodeType(QrCodeType.TABLE_QR);
        qrCode.setQrValue("QR-TABLE-001");
        qrCode.setQrUrl("http://localhost:3000/table/QR-TABLE-001");
        qrCode.setStatus(true);
        qrCode.setDeleted(false);

        QrCodeResponse expectedResponse = QrCodeResponse.builder()
                .id(qrCodeId)
                .restaurantId(restaurantId)
                .qrCodeType(QrCodeType.TABLE_QR)
                .qrValue("QR-TABLE-001")
                .qrUrl("http://localhost:3000/table/QR-TABLE-001")
                .status(true)
                .build();

        when(restaurantRepository.findByIdAndDeletedFalse(restaurantId))
                .thenReturn(Optional.of(restaurant));

        when(restaurantTableRepository.findByIdAndDeletedFalse(tableId))
                .thenReturn(Optional.of(table));

        when(qrCodeRepository.existsByRestaurant_IdAndRestaurantTable_IdAndDeletedFalse(
                restaurantId,
                tableId
        )).thenReturn(false);

        when(qrGeneratorAdapter.generateQrValue())
                .thenReturn("QR-TABLE-001");

        when(qrCodeRepository.existsByQrValueAndDeletedFalse("QR-TABLE-001"))
                .thenReturn(false);

        when(qrGeneratorAdapter.generateTableQrUrl("QR-TABLE-001"))
                .thenReturn("http://localhost:3000/table/QR-TABLE-001");

        when(qrCodeFactory.createTableQr(
                restaurant,
                table,
                "QR-TABLE-001",
                "http://localhost:3000/table/QR-TABLE-001"
        )).thenReturn(qrCode);

        when(qrCodeRepository.save(qrCode))
                .thenReturn(qrCode);

        when(qrCodeMapper.toResponse(qrCode))
                .thenReturn(expectedResponse);

        QrCodeResponse response = qrCodeService.createQrCode(restaurantId, request);

        assertNotNull(response);
        assertEquals(qrCodeId, response.id());
        assertEquals(restaurantId, response.restaurantId());
        assertEquals(QrCodeType.TABLE_QR, response.qrCodeType());
        assertEquals(QrCodeType.TABLE_QR, response.qrCodeType());
        assertEquals("QR-TABLE-001", response.qrValue());
        assertEquals("http://localhost:3000/table/QR-TABLE-001", response.qrUrl());
        assertTrue(response.status());

        verify(qrCodeRepository).save(qrCode);
        verify(eventPublisher).publishEvent(any(QrCodeChangedEvent.class));
    }

    @Test
    void testCreateTableQrCode_TableIdRequired() {
        UUID restaurantId = UUID.randomUUID();

        CreateQrCodeRequest request = CreateQrCodeRequest.builder()
                .qrCodeType(QrCodeType.TABLE_QR)
                .tableId(null)
                .build();

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        when(restaurantRepository.findByIdAndDeletedFalse(restaurantId))
                .thenReturn(Optional.of(restaurant));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> qrCodeService.createQrCode(restaurantId, request)
        );

        assertEquals("400 BAD_REQUEST", exception.getStatusCode().toString());

        verify(qrCodeRepository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void testCreateTableQrCode_TableBelongsToAnotherRestaurant() {
        UUID restaurantId = UUID.randomUUID();
        UUID anotherRestaurantId = UUID.randomUUID();
        UUID tableId = UUID.randomUUID();

        CreateQrCodeRequest request = CreateQrCodeRequest.builder()
                .qrCodeType(QrCodeType.TABLE_QR)
                .tableId(tableId)
                .build();

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        Restaurant anotherRestaurant = new Restaurant();
        anotherRestaurant.setId(anotherRestaurantId);

        RestaurantTable table = new RestaurantTable();
        table.setId(tableId);
        table.setRestaurant(anotherRestaurant);

        when(restaurantRepository.findByIdAndDeletedFalse(restaurantId))
                .thenReturn(Optional.of(restaurant));

        when(restaurantTableRepository.findByIdAndDeletedFalse(tableId))
                .thenReturn(Optional.of(table));

        assertThrows(
                ConflictException.class,
                () -> qrCodeService.createQrCode(restaurantId, request)
        );

        verify(qrCodeRepository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void testGetQrCodesByRestaurant_Success() {
        UUID restaurantId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        QrCode qrCode = new QrCode();
        qrCode.setRestaurant(restaurant);
        qrCode.setQrCodeType(QrCodeType.PUBLIC_MENU);
        qrCode.setQrValue("QR-001");

        Page<QrCode> qrCodePage = new PageImpl<>(List.of(qrCode), pageable, 1);

        QrCodeResponse response = QrCodeResponse.builder()
                .restaurantId(restaurantId)
                .qrCodeType(QrCodeType.PUBLIC_MENU)
                .qrValue("QR-001")
                .build();

        when(restaurantRepository.findByIdAndDeletedFalse(restaurantId))
                .thenReturn(Optional.of(restaurant));

        when(qrCodeRepository.findByRestaurantIdAndDeletedFalse(restaurantId, pageable))
                .thenReturn(qrCodePage);

        when(qrCodeMapper.toResponse(qrCode))
                .thenReturn(response);

        Page<QrCodeResponse> result = qrCodeService.getQrCodesByRestaurant(restaurantId, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("QR-001", result.getContent().getFirst().qrValue());

        verify(qrCodeRepository).findByRestaurantIdAndDeletedFalse(restaurantId, pageable);
    }

    @Test
    void testGetQrCodeById_Success() {
        UUID qrCodeId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        QrCode qrCode = new QrCode();
        qrCode.setId(qrCodeId);
        qrCode.setRestaurant(restaurant);
        qrCode.setQrCodeType(QrCodeType.PUBLIC_MENU);
        qrCode.setQrValue("QR-001");

        QrCodeResponse expectedResponse = QrCodeResponse.builder()
                .id(qrCodeId)
                .restaurantId(restaurantId)
                .qrCodeType(QrCodeType.PUBLIC_MENU)
                .qrValue("QR-001")
                .build();

        when(qrCodeRepository.findByIdAndDeletedFalse(qrCodeId))
                .thenReturn(Optional.of(qrCode));

        when(qrCodeMapper.toResponse(qrCode))
                .thenReturn(expectedResponse);

        QrCodeResponse response = qrCodeService.getQrCodeById(qrCodeId);

        assertNotNull(response);
        assertEquals(qrCodeId, response.id());
        assertEquals("QR-001", response.qrValue());
    }

    @Test
    void testGetQrCodeById_NotFound() {
        UUID qrCodeId = UUID.randomUUID();

        when(qrCodeRepository.findByIdAndDeletedFalse(qrCodeId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> qrCodeService.getQrCodeById(qrCodeId)
        );
    }

    @Test
    void testGetQrCodeByValue_Success() {
        UUID qrCodeId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        QrCode qrCode = new QrCode();
        qrCode.setId(qrCodeId);
        qrCode.setRestaurant(restaurant);
        qrCode.setQrCodeType(QrCodeType.PUBLIC_MENU);
        qrCode.setQrValue("QR-001");
        qrCode.setStatus(true);

        QrCodeResponse expectedResponse = QrCodeResponse.builder()
                .id(qrCodeId)
                .restaurantId(restaurantId)
                .qrCodeType(QrCodeType.PUBLIC_MENU)
                .qrValue("QR-001")
                .status(true)
                .build();

        when(qrCodeRepository.findByQrValueAndDeletedFalse("QR-001"))
                .thenReturn(Optional.of(qrCode));

        when(qrCodeMapper.toResponse(qrCode))
                .thenReturn(expectedResponse);

        QrCodeResponse response = qrCodeService.getQrCodeByValue("QR-001");

        assertNotNull(response);
        assertEquals("QR-001", response.qrValue());
        assertTrue(response.status());
    }

    @Test
    void testGetQrCodeByValue_Inactive() {
        UUID restaurantId = UUID.randomUUID();

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        QrCode qrCode = new QrCode();
        qrCode.setRestaurant(restaurant);
        qrCode.setQrCodeType(QrCodeType.PUBLIC_MENU);
        qrCode.setQrValue("QR-001");
        qrCode.setStatus(false);

        when(qrCodeRepository.findByQrValueAndDeletedFalse("QR-001"))
                .thenReturn(Optional.of(qrCode));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> qrCodeService.getQrCodeByValue("QR-001")
        );

        assertEquals("403 FORBIDDEN", exception.getStatusCode().toString());
    }

    @Test
    void testUpdateQrCodeStatus_Success() {
        UUID qrCodeId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();

        UpdateQrCodeStatusRequest request = UpdateQrCodeStatusRequest.builder()
                .status(false)
                .build();

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        QrCode qrCode = new QrCode();
        qrCode.setId(qrCodeId);
        qrCode.setRestaurant(restaurant);
        qrCode.setQrCodeType(QrCodeType.PUBLIC_MENU);
        qrCode.setQrValue("QR-001");
        qrCode.setStatus(true);

        QrCodeResponse expectedResponse = QrCodeResponse.builder()
                .id(qrCodeId)
                .restaurantId(restaurantId)
                .qrCodeType(QrCodeType.PUBLIC_MENU)
                .qrValue("QR-001")
                .status(false)
                .build();

        when(qrCodeRepository.findByIdAndDeletedFalse(qrCodeId))
                .thenReturn(Optional.of(qrCode));

        when(qrCodeRepository.save(qrCode))
                .thenReturn(qrCode);

        when(qrCodeMapper.toResponse(qrCode))
                .thenReturn(expectedResponse);

        QrCodeResponse response = qrCodeService.updateQrCodeStatus(qrCodeId, request);

        assertNotNull(response);
        assertFalse(response.status());
        assertFalse(qrCode.getStatus());

        verify(qrCodeRepository).save(qrCode);
        verify(eventPublisher).publishEvent(any(QrCodeChangedEvent.class));
    }

    @Test
    void testDeleteQrCode_Success() {
        UUID qrCodeId = UUID.randomUUID();
        UUID restaurantId = UUID.randomUUID();

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        QrCode qrCode = new QrCode();
        qrCode.setId(qrCodeId);
        qrCode.setRestaurant(restaurant);
        qrCode.setQrCodeType(QrCodeType.PUBLIC_MENU);
        qrCode.setQrValue("QR-001");
        qrCode.setDeleted(false);

        when(qrCodeRepository.findByIdAndDeletedFalse(qrCodeId))
                .thenReturn(Optional.of(qrCode));

        when(qrCodeRepository.save(qrCode))
                .thenReturn(qrCode);

        qrCodeService.deleteQrCode(qrCodeId);

        assertTrue(qrCode.getDeleted());

        verify(qrCodeRepository).save(qrCode);
        verify(eventPublisher).publishEvent(any(QrCodeChangedEvent.class));
    }

    @Test
    void testDeleteQrCode_NotFound() {
        UUID qrCodeId = UUID.randomUUID();

        when(qrCodeRepository.findByIdAndDeletedFalse(qrCodeId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> qrCodeService.deleteQrCode(qrCodeId)
        );

        verify(qrCodeRepository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any());
    }
}