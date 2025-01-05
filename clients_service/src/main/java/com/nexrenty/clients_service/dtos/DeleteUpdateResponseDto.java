package com.nexrenty.clients_service.dtos ;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Response DTO for delete and update operations")
public class DeleteUpdateResponseDto {

    @Schema(description = "Indicates whether the operation was successful", example = "true")
    private boolean status;

    @Schema(description = "A descriptive message about the operation", example = "Number of deleted clients: 5")
    private String message;

    @Schema(description = "The number of affected records (e.g., deleted or updated clients)", example = "5")
    private int count;
}