package co.istad.smartserve.features.menuitems;

import co.istad.smartserve.features.menuitems.command.*;
import co.istad.smartserve.features.menuitems.dto.*;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(
        name = "Menu Item",
        description = "Menu item management RESTful APIs"
)
public class MenuItemController {
    private final MenuItemService menuItemService;
    private final MenuItemCommandInvoker menuItemCommandInvoker;

    @PostMapping("/restaurants/{restaurantId}/menu-items")
    @Operation(summary = "Create menu item")
    public ResponseEntity<MenuItemResponse> create(
            @PathVariable UUID restaurantId,
            @Valid @RequestBody MenuItemCreateRequest request) {
        MenuItemResponse menuItemResponse = menuItemCommandInvoker.execute(
                new CreateMenuItemCommand(restaurantId, request)
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(menuItemResponse);
    }

    @GetMapping("/restaurants/{restaurantId}/menu-items")
    @Operation(summary = "Get menu items by restaurant")
    public ResponseEntity<Page<MenuItemResponse>> findByRestaurant(
            @PathVariable UUID restaurantId,
            @RequestParam(required = false) UUID categoryId,
            @ParameterObject
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {
        System.out.println("GET MENU ITEMS HIT restaurantId = " + restaurantId);
        System.out.println("categoryId = " + categoryId);
        System.out.println("pageable = " + pageable);
        return ResponseEntity.ok(menuItemService.findByRestaurant(restaurantId, categoryId, pageable));
    }

    @GetMapping("/menu-items/{menuItemId}")
    @Operation(summary = "Get menu item by ID")
    public ResponseEntity<MenuItemResponse> findById(@PathVariable UUID menuItemId) {
        return ResponseEntity.ok(menuItemService.findById(menuItemId));
    }

    @PatchMapping("/menu-items/{menuItemId}")
    @Operation(summary = "Update menu item")
    public ResponseEntity<MenuItemResponse> update(
            @PathVariable UUID menuItemId,
            @Valid @RequestBody MenuItemUpdateRequest request
    ) {
        MenuItemResponse menuItemResponse = menuItemCommandInvoker.execute(
                new UpdateMenuItemCommand(menuItemId, request)
        );
        return ResponseEntity.ok(menuItemResponse);
    }

    @PatchMapping("/menu-items/{menuItemId}/availability")
    @Operation(summary = "Change menu item availability")
    public ResponseEntity<MenuItemResponse> changeAvailability(
            @PathVariable UUID menuItemId,
            @Valid @RequestBody MenuItemAvailabilityRequest request
    ) {
        MenuItemResponse menuItemResponse = menuItemCommandInvoker.execute(
                new ChangeMenuItemAvailabilityCommand(menuItemId, request)
        );
        return ResponseEntity.ok(menuItemResponse);
    }

    @DeleteMapping("/menu-items/{menuItemId}")
    @Operation(summary = "Delete menu item")
    public ResponseEntity<Void> delete(@PathVariable UUID menuItemId) {
        menuItemCommandInvoker.execute(new DeleteMenuItemCommand(menuItemId));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/restaurants/{restaurantId}/public-menu")
    @Operation(summary = "Get public menu")
    public ResponseEntity<List<PublicMenuNodeResponse>> getPublicMenuTree(@PathVariable UUID restaurantId) {
        return ResponseEntity.ok(menuItemService.getPublicMenuTree(restaurantId));
    }
}
