package co.istad.smartserve.features.table;

import co.istad.smartserve.exception.ConflictException;
import co.istad.smartserve.exception.ResourceNotFoundException;
import co.istad.smartserve.features.restaurant.Restaurant;
import co.istad.smartserve.features.restaurant.RestaurantRepository;
import co.istad.smartserve.features.table.dto.*;
import co.istad.smartserve.features.table.event.RestaurantTableChangedEvent;
import co.istad.smartserve.features.table.pattern.RestaurantTableCommandInvoker;
import co.istad.smartserve.features.table.pattern.RestaurantTableFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestaurantTableServiceImpl implements RestaurantTableService {
    private final RestaurantTableRepository restaurantTableRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantTableMapper restaurantTableMapper;
    private final RestaurantTableFactory restaurantTableFactory;
    private final RestaurantTableCommandInvoker restaurantTableCommandInvoker;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public RestaurantTableResponse create(UUID restaurantId, CreateRestaurantTableRequest request) {
        return restaurantTableCommandInvoker.execute(
                () -> {
                    Restaurant restaurant = restaurantRepository.findById(restaurantId)
                            .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
                    boolean exists = restaurantTableRepository
                            .existsByRestaurant_IdAndTableNumberIgnoreCaseAndDeletedFalse(
                                    restaurantId,
                                    request.tableNumber().trim()
                            );
                    if (exists) {
                        throw new ConflictException(
                                "tableNumber",
                                "Table number already exists in this restaurant"
                        );
                    }
                    RestaurantTable restaurantTable = restaurantTableFactory.create(restaurant, request);
                    RestaurantTable saved = restaurantTableRepository.save(restaurantTable);
                    applicationEventPublisher.publishEvent(
                            new RestaurantTableChangedEvent(
                                    saved.getId(),
                                    restaurantId,
                                    "CREATE"
                            )
                    );
                    return restaurantTableMapper.toRestaurantTableResponse(saved);
                }
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RestaurantTableResponse> findByRestaurant(UUID restaurantId, Pageable pageable) {
        return restaurantTableRepository
                .findByRestaurantIdAndDeletedFalse(restaurantId, pageable)
                .map(restaurantTableMapper::toRestaurantTableResponse);
    }

    private RestaurantTable findTableOrThrow(UUID tableId) {
        return restaurantTableRepository.findByIdAndDeletedFalse(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant table was not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public RestaurantTableResponse findById(UUID tableId) {
        RestaurantTable restaurantTable = findTableOrThrow(tableId);
        return restaurantTableMapper.toRestaurantTableResponse(restaurantTable);
    }

    @Override
    @Transactional
    public RestaurantTableResponse update(UUID tableId, UpdateRestaurantTableRequest request) {
        RestaurantTable restaurantTable = findTableOrThrow(tableId);
        if(request.tableNumber() != null && !request.tableNumber().isBlank()) {
            boolean exists = restaurantTableRepository
                    .existsByRestaurant_IdAndTableNumberIgnoreCaseAndIdNotAndDeletedFalse(
                            restaurantTable.getRestaurant().getId(),
                            request.tableNumber().trim(),
                            tableId
                    );
            if (exists) {
                throw new ConflictException(
                        "tableNumber",
                        "Table number already exists in this restaurant"
                );
            }
            restaurantTable.setTableNumber(request.tableNumber().trim());
        }
        restaurantTableMapper.updateFromRequest(request, restaurantTable);
        RestaurantTable updated = restaurantTableRepository.save(restaurantTable);
        applicationEventPublisher.publishEvent(
                new RestaurantTableChangedEvent(
                        updated.getId(),
                        updated.getRestaurant().getId(),
                        "UPDATED"
                )
        );
        return restaurantTableMapper.toRestaurantTableResponse(updated);
    }

    @Override
    @Transactional
    public RestaurantTableResponse changeTableStatus(UUID tableId, ChangeTableStatusRequest request) {
        return restaurantTableCommandInvoker.execute(
                () -> {
                    RestaurantTable restaurantTable = findTableOrThrow(tableId);
                    restaurantTable.setTableStatus(request.tableStatus());
                    RestaurantTable updated = restaurantTableRepository.save(restaurantTable);
                    applicationEventPublisher.publishEvent(
                            new RestaurantTableChangedEvent(
                                    updated.getId(),
                                    updated.getRestaurant().getId(),
                                    "TABLE_STATUS_CHANGED"
                            )
                    );
                    return restaurantTableMapper.toRestaurantTableResponse(updated);
                }
        );
    }

    @Override
    @Transactional
    public RestaurantTableResponse changeActiveStatus(UUID tableId, ChangeRestaurantTableActiveStatusRequest request) {
        RestaurantTable restaurantTable = findTableOrThrow(tableId);
        restaurantTable.setStatus(request.status());
        RestaurantTable updated = restaurantTableRepository.save(restaurantTable);
        applicationEventPublisher.publishEvent(
                new RestaurantTableChangedEvent(
                        updated.getId(),
                        updated.getRestaurant().getId(),
                        "ACTIVE_STATUS_CHANGED"
                )
        );
        return restaurantTableMapper.toRestaurantTableResponse(updated);
    }

    @Override
    @Transactional
    public void delete(UUID tableId) {
        RestaurantTable restaurantTable = findTableOrThrow(tableId);
        restaurantTable.setDeleted(true);
        restaurantTable.setStatus(false);
        restaurantTable.setTableStatus(TableStatus.UNAVAILABLE);
        restaurantTableRepository.save(restaurantTable);
        applicationEventPublisher.publishEvent(
                new RestaurantTableChangedEvent(
                        restaurantTable.getId(),
                        restaurantTable.getRestaurant().getId(),
                        "DELETED"
                )
        );
    }
}
