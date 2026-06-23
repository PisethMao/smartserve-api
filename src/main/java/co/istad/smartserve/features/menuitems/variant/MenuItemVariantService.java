package co.istad.smartserve.features.menuitems.variant;

import co.istad.smartserve.features.menuitems.variant.dto.MenuItemVariantCreateRequest;
import co.istad.smartserve.features.menuitems.variant.dto.MenuItemVariantResponse;
import co.istad.smartserve.features.menuitems.variant.dto.MenuItemVariantStatusRequest;
import co.istad.smartserve.features.menuitems.variant.dto.MenuItemVariantUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MenuItemVariantService {
    MenuItemVariantResponse createMenuItemVariant(UUID menuItemId, MenuItemVariantCreateRequest request);

    MenuItemVariantResponse findById(UUID variantId);

    Page<MenuItemVariantResponse> findByMenuItem(UUID menuItemId, Pageable pageable);

    MenuItemVariantResponse updateMenuItemVariant(UUID variantId, MenuItemVariantUpdateRequest request);

    MenuItemVariantResponse changeStatus(UUID variantId, MenuItemVariantStatusRequest request);

    void deleteMenuItemVariant(UUID variantId);

    MenuItemVariant findVariant(UUID variantId);
}
