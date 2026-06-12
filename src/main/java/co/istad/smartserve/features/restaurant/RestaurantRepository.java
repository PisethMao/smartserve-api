package co.istad.smartserve.features.restaurant;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {
    Boolean existsByNameEnIgnoreCaseAndDeletedFalse(String name);

    Boolean existsByNameEnIgnoreCaseAndDeletedFalseAndIdNot(String nameEn, UUID id);

    List<Restaurant> findAllByDeletedFalseOrderByCreatedAtDesc();

    Optional<Restaurant> findByIdAndDeletedFalse(UUID id);
}
