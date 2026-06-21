package co.istad.smartserve.features.category;

import co.istad.smartserve.features.category.command.CreateCategoryCommand;
import co.istad.smartserve.features.category.command.UpdateCategoryCommand;
import co.istad.smartserve.features.category.dto.CategoryResponse;
import co.istad.smartserve.features.category.dto.ReorderCategoryRequest;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    CategoryResponse createCategory(CreateCategoryCommand createCategoryCommand);

    CategoryResponse updateCategory(UpdateCategoryCommand updateCategoryCommand);

    CategoryResponse getCategoryById(UUID id);

    List<CategoryResponse> getCategoriesByRestaurant(UUID restaurantId);

    void deleteCategory(UUID categoryId);

    void changeStatus(UUID categoryId, Boolean status);

    void reOrderCategories(UUID restaurantId, List<ReorderCategoryRequest> reorderCategoryRequests);
}
