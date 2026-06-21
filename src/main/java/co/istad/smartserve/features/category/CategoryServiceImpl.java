package co.istad.smartserve.features.category;

import co.istad.smartserve.exception.ConflictException;
import co.istad.smartserve.exception.ResourceNotFoundException;
import co.istad.smartserve.features.category.command.CreateCategoryCommand;
import co.istad.smartserve.features.category.command.UpdateCategoryCommand;
import co.istad.smartserve.features.category.dto.CategoryResponse;
import co.istad.smartserve.features.category.dto.ReorderCategoryRequest;
import co.istad.smartserve.features.category.event.CategoryChangedEvent;
import co.istad.smartserve.features.category.factory.CategoryFactory;
import co.istad.smartserve.features.restaurant.Restaurant;
import co.istad.smartserve.features.restaurant.RestaurantRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;
    private final CategoryMapper categoryMapper;
    private final CategoryFactory categoryFactory;
    private final ApplicationEventPublisher publisher;

    @Override
    @Transactional
    public CategoryResponse createCategory(CreateCategoryCommand createCategoryCommand) {
        Restaurant restaurant = restaurantRepository.findByIdAndDeletedFalse(createCategoryCommand.restaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
        if (categoryRepository.existsByRestaurant_IdAndNameEnIgnoreCaseAndDeletedFalse(createCategoryCommand.restaurantId(), createCategoryCommand.nameEn())) {
            throw new ConflictException("Category with name " + createCategoryCommand.nameEn() + " already exists in the restaurant.");
        }
        Integer displayOrder = createCategoryCommand.displayOrder() != null
                ? createCategoryCommand.displayOrder()
                : categoryRepository.getNextDisplayOrder(createCategoryCommand.restaurantId());
        if (categoryRepository.existsByRestaurant_IdAndDisplayOrderAndDeletedFalse(
                createCategoryCommand.restaurantId(),
                displayOrder
        )) {
            throw new ConflictException("Display order " + displayOrder + " already exists in this restaurant.");
        }
        Category category = categoryFactory.create(
                restaurant,
                createCategoryCommand.nameEn(),
                createCategoryCommand.nameKh(),
                createCategoryCommand.iconUrl(),
                displayOrder,
                createCategoryCommand.status()
        );
        Category savedCategory = categoryRepository.save(category);
        publisher.publishEvent(
                new CategoryChangedEvent(createCategoryCommand.restaurantId(), savedCategory.getId(), "CREATED")
        );
        return categoryMapper.toResponse(savedCategory);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(UpdateCategoryCommand updateCategoryCommand) {
        Category category = categoryRepository.findByIdAndDeletedFalse(updateCategoryCommand.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        UUID restaurantId = category.getRestaurant().getId();
        if (categoryRepository.existsByRestaurant_IdAndNameEnIgnoreCaseAndIdNotAndDeletedFalse(restaurantId, updateCategoryCommand.nameEn(), updateCategoryCommand.categoryId())) {
            throw new ConflictException("Category with name " + updateCategoryCommand.nameEn() + " already exists in this restaurant.");
        }
        category.update(
                updateCategoryCommand.nameEn(),
                updateCategoryCommand.nameKh(),
                updateCategoryCommand.iconUrl(),
                updateCategoryCommand.displayOrder(),
                updateCategoryCommand.status() != null ? updateCategoryCommand.status() : category.getStatus()
        );
        Category updatedCategory = categoryRepository.save(category);
        publisher.publishEvent(
                new CategoryChangedEvent(restaurantId, updatedCategory.getId(), "UPDATED")
        );

        return categoryMapper.toResponse(updatedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(UUID id) {
        Category category = categoryRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return categoryMapper.toResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategoriesByRestaurant(UUID restaurantId) {
        if(!restaurantRepository.existsById(restaurantId)) {
            throw new ResourceNotFoundException("Restaurant not found");
        }
        return categoryRepository
                .findByRestaurant_IdAndDeletedFalseOrderByDisplayOrderAscCreatedAtDesc(restaurantId)
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional()
    public void deleteCategory(UUID categoryId) {
        Category category = categoryRepository.findByIdAndDeletedFalse(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        category.setDeleted(true);
        categoryRepository.save(category);
        publisher.publishEvent(
                new CategoryChangedEvent(category.getRestaurant().getId(), category.getId(), "DELETED")
        );
    }

    @Override
    @Transactional()
    public void changeStatus(UUID categoryId, Boolean status) {
        Category category = categoryRepository.findByIdAndDeletedFalse(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        if(Boolean.TRUE.equals(status)) {
            category.enable();
        }else  {
            category.disable();
        }
        categoryRepository.save(category);
        publisher.publishEvent(
                new CategoryChangedEvent(category.getRestaurant().getId(), category.getId(), "STATUS_CHANGED")
        );
    }

    @Override
    @Transactional()
    public void reOrderCategories(UUID restaurantId, List<ReorderCategoryRequest> reorderCategoryRequests) {
        for (ReorderCategoryRequest reorderCategoryRequest : reorderCategoryRequests) {
            Category category = categoryRepository.findByIdAndDeletedFalse(reorderCategoryRequest.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            if(!category.getRestaurant().getId().equals(restaurantId)) {
                throw new ConflictException("Category does not belong to restaurant " + restaurantId);
            }
            category.setDisplayOrder(reorderCategoryRequest.displayOrder());
            categoryRepository.save(category);
        }
        publisher.publishEvent(
                new CategoryChangedEvent(restaurantId, null, "REORDERED")
        );
    }
}
