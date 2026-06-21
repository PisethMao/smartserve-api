package co.istad.smartserve.features.category;

import co.istad.smartserve.features.category.command.CreateCategoryCommand;
import co.istad.smartserve.features.category.command.UpdateCategoryCommand;
import co.istad.smartserve.features.category.dto.CategoryResponse;
import co.istad.smartserve.features.category.dto.CreateCategoryRequest;
import co.istad.smartserve.features.category.dto.ReorderCategoryRequest;
import co.istad.smartserve.features.category.dto.UpdateCategoryRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Category", description = "Category management RESTful APIs")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/restaurants/{restaurantId}/categories")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create category", description = "Create a new category for a restaurant.")
    public CategoryResponse createCategory(
            @PathVariable UUID restaurantId,
            @Valid @RequestBody CreateCategoryRequest createCategoryRequest
    ) {
        return categoryService.createCategory(
                new CreateCategoryCommand(
                        restaurantId,
                        createCategoryRequest.nameEn(),
                        createCategoryRequest.nameKh(),
                        createCategoryRequest.iconUrl(),
                        createCategoryRequest.displayOrder(),
                        createCategoryRequest.status()
                )
        );
    }

    @GetMapping("/restaurants/{restaurantId}/categories")
    @Operation(summary = "Get categories by restaurant", description = "Get all categories that belong to a restaurant.")
    public List<CategoryResponse> getCategoriesByRestaurant(@PathVariable UUID restaurantId) {
        return categoryService.getCategoriesByRestaurant(restaurantId);
    }

    @GetMapping("/categories/{id}")
    @Operation(summary = "Get category by ID", description = "Get one category by its ID.")
    public CategoryResponse getCategoryById(@PathVariable UUID id) {
        return categoryService.getCategoryById(id);
    }

    @PutMapping("/categories/{categoryId}")
    @Operation(summary = "Update category", description = "Update category information.")
    public CategoryResponse updateCategory(
            @PathVariable UUID categoryId,
            @Valid @RequestBody UpdateCategoryRequest request
    ) {
        return categoryService.updateCategory(
                new UpdateCategoryCommand(
                        categoryId,
                        request.nameEn(),
                        request.nameKh(),
                        request.iconUrl(),
                        request.displayOrder(),
                        request.status()
                )
        );
    }

    @PatchMapping("/categories/{categoryId}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Change category status", description = "Enable or disable a category.")
    public void changeStatus(@PathVariable UUID categoryId, @RequestParam Boolean status) {
        categoryService.changeStatus(categoryId, status);
    }

    @PatchMapping("/restaurants/{restaurantId}/categories/reorder")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Reorder categories", description = "Change category display order for a restaurant.")
    public void reorderCategories(
            @PathVariable UUID restaurantId,
            @Valid @RequestBody List<ReorderCategoryRequest> requests
    ) {
        System.out.println("REORDER CONTROLLER HIT");
        System.out.println("restaurantId = " + restaurantId);
        System.out.println("requests = " + requests);
        categoryService.reOrderCategories(restaurantId, requests);
    }

    @DeleteMapping("/categories/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete category", description = "Soft delete a category.")
    public void deleteCategory(
            @PathVariable UUID categoryId
    ) {
        categoryService.deleteCategory(categoryId);
    }
}
