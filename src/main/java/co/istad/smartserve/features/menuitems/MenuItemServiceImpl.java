package co.istad.smartserve.features.menuitems;

import co.istad.smartserve.exception.ConflictException;
import co.istad.smartserve.features.category.Category;
import co.istad.smartserve.features.category.CategoryRepository;
import co.istad.smartserve.features.menuitems.composite.MenuCategoryComposite;
import co.istad.smartserve.features.menuitems.composite.MenuComponent;
import co.istad.smartserve.features.menuitems.composite.MenuItemLeaf;
import co.istad.smartserve.features.menuitems.dto.*;
import co.istad.smartserve.features.menuitems.event.MenuItemChangedEvent;
import co.istad.smartserve.features.menuitems.factory.MenuItemFactory;
import co.istad.smartserve.features.restaurant.Restaurant;
import co.istad.smartserve.features.restaurant.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuItemServiceImpl implements MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final MenuItemMapper menuItemMapper;
    private final MenuItemFactory menuItemFactory;
    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public MenuItemResponse create(UUID restaurantId, MenuItemCreateRequest request) {
        System.out.println("CREATE MENU ITEM SERVICE HIT");
        System.out.println("restaurantId = " + restaurantId);
        System.out.println("categoryId = " + request.categoryId());
        System.out.println("nameEn = " + request.nameEn());
        Restaurant restaurant = findRestaurant(restaurantId);
        Category category = findCategory(request.categoryId());
        validateCategoryBelongsToRestaurant(category, restaurantId);
        boolean duplicated = menuItemRepository.existsByRestaurantAndCategoryAndNameEn(
                restaurantId,
                category.getId(),
                request.nameEn()
        );
        System.out.println("duplicated = " + duplicated);
        if (duplicated) {
            System.out.println("THROWING 409 CONFLICT");
            throw new ConflictException(
                    "Menu item with name '" + request.nameEn() + "' already exists in this category",
                    Map.of("nameEn", "Menu item name already exists in this category")
            );
        }
        MenuItem menuItem = menuItemFactory.createMenuItem(restaurant, category, request);
        MenuItem savedMenuItem = menuItemRepository.save(menuItem);
        publishEvent(savedMenuItem, "Created MenuItem");
        return menuItemMapper.toMenuItemResponse(savedMenuItem);
    }

    @Override
    public MenuItemResponse findById(UUID menuItemId) {
        MenuItem menuItem = findMenuItems(menuItemId);
        return menuItemMapper.toMenuItemResponse(menuItem);
    }

    @Override
    public Page<MenuItemResponse> findByRestaurant(UUID restaurantId, UUID categoryId, Pageable pageable) {
        findRestaurant(restaurantId);
        Page<MenuItem> menuItems;
        if (categoryId != null) {
            menuItems = menuItemRepository
                    .findByRestaurantIdAndCategoryIdAndNotDeleted(restaurantId, categoryId, pageable);
        } else {
            menuItems = menuItemRepository.findByRestaurantIdAndNotDeleted(restaurantId, pageable);
        }
        return menuItems.map(menuItemMapper::toMenuItemResponse);
    }

    @Override
    @Transactional
    public MenuItemResponse update(UUID menuItemId, MenuItemUpdateRequest request) {
        MenuItem menuItem = findMenuItems(menuItemId);
        UUID restaurantId = menuItem.getRestaurant().getId();
        Category targetCategory = menuItem.getCategory();
        if (request.categoryId() != null) {
            targetCategory = findCategory(request.categoryId());
            validateCategoryBelongsToRestaurant(targetCategory, restaurantId);
        }
        String targetNameEn = request.nameEn() != null
                ? request.nameEn()
                : menuItem.getNameEn();
        if (menuItemRepository.existsByRestaurantAndCategoryAndNameEnExceptSelf(
                restaurantId,
                targetCategory.getId(),
                targetNameEn,
                menuItemId
        )) {
            throw new ConflictException(
                    "Menu item with name '" + targetNameEn + "' already exists in this category",
                    Map.of("nameEn", "Menu item name already exists in this category")
            );
        }
        if (request.categoryId() != null) {
            menuItem.setCategory(targetCategory);
        }
        if (request.nameEn() != null) {
            menuItem.setNameEn(request.nameEn());
        }
        if (request.nameKh() != null) {
            menuItem.setNameKh(request.nameKh());
        }
        if (request.descriptionEn() != null) {
            menuItem.setDescriptionEn(request.descriptionEn());
        }
        if (request.descriptionKh() != null) {
            menuItem.setDescriptionKh(request.descriptionKh());
        }
        if (request.price() != null) {
            menuItem.setPrice(request.price());
        }
        if (request.imageUrl() != null) {
            menuItem.setImageUrl(request.imageUrl());
        }
        if (request.soldLimit() != null) {
            menuItem.setSoldLimit(request.soldLimit());
        }
        if (request.availabilityStatus() != null) {
            menuItem.setMenuItemAvailabilityStatus(request.availabilityStatus());
        }
        if (request.status() != null) {
            menuItem.setStatus(request.status());
        }
        MenuItem updatedMenuItem = menuItemRepository.save(menuItem);
        publishEvent(updatedMenuItem, "Updated MenuItem");
        return menuItemMapper.toMenuItemResponse(updatedMenuItem);
    }

    @Override
    @Transactional
    public MenuItemResponse changeAvailability(UUID menuItemId, MenuItemAvailabilityRequest request) {
        MenuItem menuItem = findMenuItems(menuItemId);
        menuItem.setMenuItemAvailabilityStatus(request.availabilityStatus());
        MenuItem updatedMenuItem = menuItemRepository.save(menuItem);
        publishEvent(updatedMenuItem, "AVAILABILITY_CHANGED");
        return menuItemMapper.toMenuItemResponse(updatedMenuItem);
    }

    @Override
    @Transactional
    public void delete(UUID menuItemId) {
        MenuItem menuItem = findMenuItems(menuItemId);
        menuItem.setDeleted(true);
        menuItemRepository.save(menuItem);
        publishEvent(menuItem, "Deleted MenuItem");
    }

    @Override
    public List<PublicMenuNodeResponse> getPublicMenuTree(UUID restaurantId) {
        findRestaurant(restaurantId);
        List<MenuItem> menuItems = menuItemRepository.findPublicMenuByRestaurantId(restaurantId, MenuItemAvailabilityStatus.UNAVAILABLE);
        Map<UUID, MenuCategoryComposite> map = new LinkedHashMap<>();
        for (MenuItem menuItem : menuItems) {
            Category category = menuItem.getCategory();
            MenuCategoryComposite menuCategoryComposite = map.computeIfAbsent(
                    category.getId(), ignored -> new MenuCategoryComposite(category)
            );
            menuCategoryComposite.addComponent(new MenuItemLeaf(menuItem));
        }
        return map.values()
                .stream()
                .map(MenuComponent::toNodeResponse)
                .toList();
    }

    @Override
    public MenuItem findMenuItems(UUID menuItemId) {
        return menuItemRepository.findByIdAndNotDeleted(menuItemId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "MenuItem with id " + menuItemId + " not found"
                ));
    }

    @Override
    public Restaurant findRestaurant(UUID restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Restaurant with id " + restaurantId + " not found"
                ));
    }

    @Override
    public Category findCategory(UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Category with id " + categoryId + " not found"
                ));
    }

    @Override
    public void validateCategoryBelongsToRestaurant(Category category, UUID restaurantId) {
        if (!category.getRestaurant().getId().equals(restaurantId)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Category does not belong to restaurant with id " + restaurantId
            );
        }
    }

    @Override
    public void publishEvent(MenuItem menuItem, String action) {
        applicationEventPublisher.publishEvent(new MenuItemChangedEvent(
                menuItem.getRestaurant().getId(),
                menuItem.getCategory().getId(),
                menuItem.getId(),
                action
        ));
    }
}
