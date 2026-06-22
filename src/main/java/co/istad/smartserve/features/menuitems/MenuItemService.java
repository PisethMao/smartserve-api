package co.istad.smartserve.features.menuitems;

import co.istad.smartserve.features.category.Category;
import co.istad.smartserve.features.menuitems.dto.*;
import co.istad.smartserve.features.restaurant.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface MenuItemService {
    MenuItemResponse create(UUID restaurantId, MenuItemCreateRequest request);
    MenuItemResponse findById(UUID menuItemId);
    Page<MenuItemResponse> findByRestaurant(UUID restaurantId, UUID categoryId, Pageable pageable);
    MenuItemResponse update(UUID menuItemId, MenuItemUpdateRequest request);
    MenuItemResponse changeAvailability(UUID menuItemId, MenuItemAvailabilityRequest request);
    void delete(UUID menuItemId);
    List<PublicMenuNodeResponse> getPublicMenuTree(UUID restaurantId);
    MenuItem findMenuItems(UUID menuItemId);
    Restaurant findRestaurant(UUID restaurantId);
    Category findCategory(UUID categoryId);
    void validateCategoryBelongsToRestaurant(Category category, UUID restaurantId);
    void publishEvent(MenuItem menuItem, String action);
}
