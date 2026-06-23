package co.istad.smartserve.features.menuitems.variant;

import co.istad.smartserve.features.menuitems.variant.command.*;
import co.istad.smartserve.features.menuitems.variant.dto.MenuItemVariantCreateRequest;
import co.istad.smartserve.features.menuitems.variant.dto.MenuItemVariantResponse;
import co.istad.smartserve.features.menuitems.variant.dto.MenuItemVariantStatusRequest;
import co.istad.smartserve.features.menuitems.variant.dto.MenuItemVariantUpdateRequest;
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

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Menu Item Variants", description = "Menu item variant management APIs")
public class MenuItemVariantController {
    private final MenuItemVariantService menuItemVariantService;
    private final MenuItemVariantCommandInvoker menuItemVariantCommandInvoker;

    @PostMapping("/menu-items/{menuItemId}/variants")
    @Operation(summary = "Create menu item variant")
    public ResponseEntity<MenuItemVariantResponse> createMenuItemVariant(
            @PathVariable UUID menuItemId,
            @Valid @RequestBody MenuItemVariantCreateRequest request
            ){
        MenuItemVariantResponse menuItemVariantResponse = menuItemVariantCommandInvoker.invoke(
                new CreateMenuItemVariantCommand(menuItemVariantService, menuItemId, request)
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(menuItemVariantResponse);
    }

    @GetMapping("/menu-items/{menuItemId}/variants")
    @Operation(summary = "Get variants by menu item")
    public ResponseEntity<Page<MenuItemVariantResponse>> findByMenuItem(
            @PathVariable UUID menuItemId,
            @ParameterObject
            @PageableDefault(
                    sort = "displayOrder",
                    direction = Sort.Direction.ASC
            )
            Pageable pageable
    ){
        return ResponseEntity.ok(menuItemVariantService.findByMenuItem(menuItemId, pageable));
    }

    @GetMapping("/menu-item-variants/{variantId}")
    @Operation(summary = "Get menu item variant by ID")
    public ResponseEntity<MenuItemVariantResponse> findById(
            @PathVariable UUID variantId
    ){
        return ResponseEntity.ok(menuItemVariantService.findById(variantId));
    }

    @PutMapping("/menu-item-variants/{variantId}")
    @Operation(summary = "Update menu item variant")
    public ResponseEntity<MenuItemVariantResponse> updateMenuItemVariant(
            @PathVariable UUID variantId,
            @Valid @RequestBody MenuItemVariantUpdateRequest request
    ){
        MenuItemVariantResponse menuItemVariantResponse = menuItemVariantCommandInvoker.invoke(
                new UpdateMenuItemVariantCommand(menuItemVariantService, variantId, request)
        );
        return ResponseEntity.ok(menuItemVariantResponse);
    }

    @PatchMapping("/menu-item-variants/{variantId}/status")
    @Operation(summary = "Change menu item variant status")
    public ResponseEntity<MenuItemVariantResponse> changeMenuItemVariantStatus(
            @PathVariable UUID variantId,
            @Valid @RequestBody MenuItemVariantStatusRequest request
    ){
        MenuItemVariantResponse menuItemVariantResponse = menuItemVariantCommandInvoker.invoke(
                new ChangeMenuItemVariantStatusCommand(menuItemVariantService, variantId, request)
        );
        return ResponseEntity.ok(menuItemVariantResponse);
    }

    @DeleteMapping("/menu-item-variants/{variantId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete menu item variant")
    public void deleteMenuItemVariant(
            @PathVariable UUID variantId
    ){
        menuItemVariantCommandInvoker.invoke(
                new DeleteMenuItemVariantCommand(menuItemVariantService, variantId)
        );
    }
}
