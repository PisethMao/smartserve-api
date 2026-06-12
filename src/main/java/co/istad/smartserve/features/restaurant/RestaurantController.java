package co.istad.smartserve.features.restaurant;

import co.istad.smartserve.features.restaurant.dto.CreateRestaurantRequest;
import co.istad.smartserve.features.restaurant.dto.RestaurantResponse;
import co.istad.smartserve.features.restaurant.dto.UpdateRestaurantRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Restaurant", description = "Restaurant management RESTful APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;

    @Operation(summary = "Create restaurant", description = "Create a new restaurant record.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public RestaurantResponse createRestaurant(@Valid @RequestBody CreateRestaurantRequest createRestaurantRequest) {
        return restaurantService.createRestaurant(createRestaurantRequest);
    }

    @Operation(summary = "Get all restaurants", description = "Get all non-deleted restaurants.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<RestaurantResponse> getRestaurants() {
        return restaurantService.getRestaurants();
    }

    @Operation(summary = "Get restaurant by ID", description = "Get one restaurant by UUID.")
    @GetMapping("/{id}")
    public RestaurantResponse getRestaurantById(@PathVariable UUID id) {
        return restaurantService.getRestaurantById(id);
    }

    @Operation(summary = "Update restaurant", description = "Update an existing restaurant by UUID.")
    @PutMapping("/{id}")
    public RestaurantResponse updateRestaurant(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateRestaurantRequest updateRestaurantRequest) {
        return restaurantService.updateRestaurant(id, updateRestaurantRequest);
    }

    @Operation(summary = "Delete restaurant", description = "Soft delete a restaurant by UUID.")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteRestaurant(@PathVariable UUID id) {
        restaurantService.deleteRestaurant(id);
    }
}
