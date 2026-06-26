package co.istad.smartserve.features.table;

import co.istad.smartserve.features.table.dto.*;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(
        name = "Restaurant Table",
        description = "Restaurant table management RESTful APIs"
)
public class RestaurantTableController {
    private final RestaurantTableService restaurantTableService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/restaurants/{restaurantId}/tables")
    @Operation(
            summary = "Create restaurant table",
            description = "Create a new table for a specific restaurant"
    )
    public RestaurantTableResponse create(
            @PathVariable UUID restaurantId,
            @Valid @RequestBody CreateRestaurantTableRequest createRestaurantTableRequest
    ) {
        return restaurantTableService.create(restaurantId, createRestaurantTableRequest);
    }

    @GetMapping("/restaurants/{restaurantId}/tables")
    @Operation(
            summary = "Get restaurant tables",
            description = "Get all non-deleted tables by restaurant ID with pagination"
    )
    public Page<RestaurantTableResponse> findByRestaurant(
            @PathVariable UUID restaurantId,
            @ParameterObject
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return restaurantTableService.findByRestaurant(restaurantId, pageable);
    }

    @GetMapping("/restaurant-tables/{tableId}")
    @Operation(
            summary = "Get restaurant table by ID",
            description = "Get one restaurant table by table ID"
    )
    public RestaurantTableResponse findById(@PathVariable UUID tableId) {
        return restaurantTableService.findById(tableId);
    }

    @PatchMapping("/restaurant-tables/{tableId}")
    @Operation(
            summary = "Update restaurant table",
            description = "Update table number, capacity, floor, zone, table status, or active status"
    )
    public RestaurantTableResponse update(
            @PathVariable UUID tableId,
            @Valid @RequestBody UpdateRestaurantTableRequest updateRestaurantTableRequest
    ) {
        return restaurantTableService.update(tableId, updateRestaurantTableRequest);
    }

    @PatchMapping("/restaurant-tables/{tableId}/table-status")
    @Operation(
            summary = "Change table status",
            description = "Change business table status such as AVAILABLE, OCCUPIED, RESERVED, or UNAVAILABLE"
    )
    public RestaurantTableResponse changeTableStatus(
            @PathVariable UUID tableId,
            @Valid @RequestBody ChangeTableStatusRequest request
    ) {
        return restaurantTableService.changeTableStatus(tableId, request);
    }

    @PatchMapping("/restaurant-tables/{tableId}/status")
    @Operation(
            summary = "Change active status",
            description = "Enable or disable a restaurant table without deleting it"
    )
    public RestaurantTableResponse changeActiveStatus(
            @PathVariable UUID tableId,
            @Valid @RequestBody ChangeRestaurantTableActiveStatusRequest request
    ) {
        return restaurantTableService.changeActiveStatus(tableId, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/restaurant-tables/{tableId}")
    @Operation(
            summary = "Delete restaurant table",
            description = "Soft delete a restaurant table by table ID"
    )
    public void delete(@PathVariable UUID tableId) {
        restaurantTableService.delete(tableId);
    }
}
