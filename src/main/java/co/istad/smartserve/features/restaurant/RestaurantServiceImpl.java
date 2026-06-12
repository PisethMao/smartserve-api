package co.istad.smartserve.features.restaurant;

import co.istad.smartserve.exception.ConflictException;
import co.istad.smartserve.exception.ResourceNotFoundException;
import co.istad.smartserve.features.restaurant.dto.CreateRestaurantRequest;
import co.istad.smartserve.features.restaurant.dto.RestaurantResponse;
import co.istad.smartserve.features.restaurant.dto.UpdateRestaurantRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    @Override
    public RestaurantResponse createRestaurant(CreateRestaurantRequest request) {
        if (restaurantRepository.existsByNameEnIgnoreCaseAndDeletedFalse(request.nameEn())) {
            throw new ConflictException("Restaurant with name " + request.nameEn() + " already exists");
        }
        Restaurant restaurant = restaurantMapper.toEntity(request);
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return restaurantMapper.toResponse(savedRestaurant);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantResponse> getRestaurants() {
        return restaurantRepository.findAllByDeletedFalseOrderByCreatedAtDesc()
                .stream()
                .map(restaurantMapper::toResponse)
                .toList();
    }

    private Restaurant getRestaurantEntityById(UUID id) {
        return restaurantRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant with id " + id + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public RestaurantResponse getRestaurantById(UUID id) {
        Restaurant restaurant = getRestaurantEntityById(id);
        return restaurantMapper.toResponse(restaurant);
    }

    @Override
    public RestaurantResponse updateRestaurant(UUID id, UpdateRestaurantRequest request) {
        Restaurant restaurant = getRestaurantEntityById(id);
        if (restaurantRepository.existsByNameEnIgnoreCaseAndDeletedFalseAndIdNot(request.nameEn(), id)) {
            throw new ConflictException("Restaurant with name " + request.nameEn() + " already exists");
        }
        restaurantMapper.updateEntityFromRequest(request, restaurant);
        Restaurant updatedRestaurant = restaurantRepository.save(restaurant);
        return restaurantMapper.toResponse(updatedRestaurant);
    }

    @Override
    public void deleteRestaurant(UUID id) {
        Restaurant restaurant = getRestaurantEntityById(id);
        restaurant.setDeleted(true);
        restaurantRepository.save(restaurant);
    }
}
