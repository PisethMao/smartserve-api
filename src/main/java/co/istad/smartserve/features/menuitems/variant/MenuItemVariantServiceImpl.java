package co.istad.smartserve.features.menuitems.variant;

import co.istad.smartserve.exception.ConflictException;
import co.istad.smartserve.exception.ResourceNotFoundException;
import co.istad.smartserve.features.menuitems.MenuItem;
import co.istad.smartserve.features.menuitems.MenuItemRepository;
import co.istad.smartserve.features.menuitems.variant.dto.MenuItemVariantCreateRequest;
import co.istad.smartserve.features.menuitems.variant.dto.MenuItemVariantResponse;
import co.istad.smartserve.features.menuitems.variant.dto.MenuItemVariantStatusRequest;
import co.istad.smartserve.features.menuitems.variant.dto.MenuItemVariantUpdateRequest;
import co.istad.smartserve.features.menuitems.variant.event.MenuItemVariantChangedEvent;
import co.istad.smartserve.features.menuitems.variant.factory.MenuItemVariantFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuItemVariantServiceImpl implements MenuItemVariantService {
    private final MenuItemVariantRepository menuItemVariantRepository;
    private final MenuItemRepository menuItemRepository;
    private final MenuItemVariantMapper menuItemVariantMapper;
    private final MenuItemVariantFactory menuItemVariantFactory;
    private final ApplicationEventPublisher applicationEventPublisher;

    private MenuItem findMenuItem(UUID menuItemId) {
        return menuItemRepository.findByIdAndNotDeleted(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Menu item with id " + menuItemId + " has not been found"
                ));
    }

    private void validateDuplicateNameForCreate(UUID menuItemId, String nameEn) {
        if (menuItemVariantRepository.existsByMenuItemIdAndNameEn(menuItemId, nameEn)) {
            throw new ConflictException(
                    "Duplicate variant name: " + nameEn,
                    Map.of("nameEn", "Variant English name already exists: " + nameEn)
            );
        }
    }

    private void validateDuplicateNameForUpdate(UUID menuItemId, UUID variantId, String nameEn) {
        if (menuItemVariantRepository.existsByMenuItemIdAndNameEnAndIdNot(menuItemId, nameEn, variantId)) {
            throw new ConflictException(
                    "Duplicate variant name: " + nameEn,
                    Map.of("nameEn", "Variant English name already exists: " + nameEn)
            );
        }
    }

    private void publishEvent(UUID menuItemId, UUID variantId, String action) {
        applicationEventPublisher.publishEvent(
                new MenuItemVariantChangedEvent(menuItemId, variantId, action)
        );
    }

    private void validateDuplicateDisplayOrderForCreate(UUID menuItemId, Integer displayOrder) {
        if (displayOrder == null) {
            return;
        }
        if (menuItemVariantRepository.existsByMenuItemIdAndDisplayOrder(menuItemId, displayOrder)) {
            throw new ConflictException(
                    "Display order already exists: " + displayOrder,
                    Map.of("displayOrder", "Display order already exists: " + displayOrder)
            );
        }
    }

    private void validateDuplicateDisplayOrderForUpdate(UUID menuItemId, UUID variantId, Integer displayOrder) {
        if (displayOrder == null) {
            return;
        }
        if (menuItemVariantRepository.existsByMenuItemIdAndDisplayOrderAndIdNot(
                menuItemId,
                displayOrder,
                variantId
        )) {
            throw new ConflictException(
                    "Display order already exists: " + displayOrder,
                    Map.of("displayOrder", "Display order already exists: " + displayOrder)
            );
        }
    }

    @Override
    @Transactional
    public MenuItemVariantResponse createMenuItemVariant(UUID menuItemId, MenuItemVariantCreateRequest request) {
        MenuItem menuItem = findMenuItem(menuItemId);
        validateDuplicateNameForCreate(menuItemId, request.nameEn());
        validateDuplicateDisplayOrderForCreate(menuItemId, request.displayOrder());
        MenuItemVariant menuItemVariant = menuItemVariantFactory.createMenuItemVariant(menuItem, request);
        MenuItemVariant savedMenuItemVariant = menuItemVariantRepository.save(menuItemVariant);
        if (Boolean.TRUE.equals(request.defaultVariant())) {
            menuItemVariantRepository.unsetOtherDefaultVariants(
                    menuItemId,
                    savedMenuItemVariant.getId()
            );
            savedMenuItemVariant.setDefaultVariant(true);
        } else {
            savedMenuItemVariant.setDefaultVariant(false);
        }
        MenuItemVariant finalSavedVariant = menuItemVariantRepository.save(savedMenuItemVariant);
        publishEvent(menuItemId, finalSavedVariant.getId(), "CREATED");
        return menuItemVariantMapper.toResponse(finalSavedVariant);
    }

    @Override
    @Transactional(readOnly = true)
    public MenuItemVariantResponse findById(UUID variantId) {
        return menuItemVariantMapper.toResponse(findVariant(variantId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MenuItemVariantResponse> findByMenuItem(UUID menuItemId, Pageable pageable) {
        findMenuItem(menuItemId);
        return menuItemVariantRepository
                .findByMenuItemIdAndNotDeleted(menuItemId, pageable)
                .map(menuItemVariantMapper::toResponse);
    }

    @Override
    @Transactional
    public MenuItemVariantResponse updateMenuItemVariant(UUID variantId, MenuItemVariantUpdateRequest request) {
        MenuItemVariant menuItemVariant = findVariant(variantId);
        UUID menuItemId = menuItemVariant.getMenuItem().getId();
        if (request.nameEn() != null && !request.nameEn().isBlank()) {
            validateDuplicateNameForUpdate(menuItemId, variantId, request.nameEn());
            menuItemVariant.setNameEn(request.nameEn());
        }
        if (request.nameKh() != null) {
            menuItemVariant.setNameKh(request.nameKh());
        }
        if (request.price() != null) {
            menuItemVariant.setPrice(request.price());
        }
        if (request.displayOrder() != null) {
            validateDuplicateDisplayOrderForUpdate(
                    menuItemId,
                    variantId,
                    request.displayOrder()
            );
            menuItemVariant.setDisplayOrder(request.displayOrder());
        }
        if (request.availabilityStatus() != null) {
            menuItemVariant.setAvailabilityStatus(request.availabilityStatus());
        }
        if (request.defaultVariant() != null) {
            if (request.defaultVariant()) {
                menuItemVariantRepository.unsetOtherDefaultVariants(
                        menuItemId,
                        variantId
                );
                menuItemVariant.setDefaultVariant(true);
            } else {
                menuItemVariant.setDefaultVariant(false);
            }
        }
        MenuItemVariant updatedMenuItemVariant = menuItemVariantRepository.save(menuItemVariant);
        publishEvent(menuItemId, updatedMenuItemVariant.getId(), "UPDATED");
        return menuItemVariantMapper.toResponse(updatedMenuItemVariant);
    }

    @Override
    @Transactional()
    public MenuItemVariantResponse changeStatus(UUID variantId, MenuItemVariantStatusRequest request) {
        MenuItemVariant menuItemVariant = findVariant(variantId);
        menuItemVariant.setAvailabilityStatus(request.availabilityStatus());
        MenuItemVariant updatedMenuItemVariant = menuItemVariantRepository.save(menuItemVariant);
        publishEvent(
                menuItemVariant.getMenuItem().getId(),
                updatedMenuItemVariant.getId(),
                "STATUS_CHANGED"
        );
        return menuItemVariantMapper.toResponse(updatedMenuItemVariant);
    }

    @Override
    @Transactional()
    public void deleteMenuItemVariant(UUID variantId) {
        MenuItemVariant menuItemVariant = findVariant(variantId);
        menuItemVariant.setDeleted(true);
        menuItemVariant.setDefaultVariant(false);
        menuItemVariantRepository.save(menuItemVariant);
        publishEvent(
                menuItemVariant.getMenuItem().getId(),
                menuItemVariant.getId(),
                "DELETED"
        );
    }

    @Override
    @Transactional(readOnly = true)
    public MenuItemVariant findVariant(UUID variantId) {
        return menuItemVariantRepository.findByIdAndNotDeleted(variantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Menu item variant with id " + variantId + " has not been found"
                ));
    }
}
