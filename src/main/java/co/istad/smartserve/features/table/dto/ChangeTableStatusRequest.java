package co.istad.smartserve.features.table.dto;

import co.istad.smartserve.features.table.TableStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeTableStatusRequest(
        @NotNull(message = "Table status is required")
        TableStatus tableStatus
) {
}
