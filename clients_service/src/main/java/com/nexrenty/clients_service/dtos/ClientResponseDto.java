package com.nexrenty.clients_service.dtos;


import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Response DTO for client data")
public class ClientResponseDto {
    
    @Schema(description = "The unique identifier of the client", example = "18899")
    private long id ;

    @Schema(description = "The first name of the client", example = "Obaid")
    private String firstname ;

    @Schema(description = "The last name of the client", example = "Doe")
    private String lastname ;

    @Schema(description = "The CIN (National ID) or passport number of the client", example = "AB123456")
    private String cinOrPassport ;

    @Schema(description = "The driving licence number of the client", example = "LIC123456")
    private String licence ;

    @Schema(description = "The nationality of the client", example = "American")
    private String nationality ;

    @Schema(description = "The address of the client", example = "123 Main St")
    private String address ;

    @Schema(description = "The city of residence of the client", example = "New York")
    private String ville ;

    @Schema(description = "The postal code of the client's address", example = "10001")
    private int codePostal ;

    @Schema(description = "The primary phone number of the client", example = "+1234567890")
    private String phone1 ;

    @Schema(description = "The secondary phone number of the client", example = "+0987654321")
    private String phone2 ;

    @Schema(description = "The email address of the client", example = "john.doe@example.com")
    private String email ;

    @Schema(description = "The expiration date of the client's CIN or passport", example = "2025-12-31T23:59:59Z")
    private Instant cinIsValideUntil ;

    @Schema(description = "The expiration date of the client's driving licence", example = "2025-12-31T23:59:59Z")
    private Instant licenceIsValideUntil ;

    @Schema(description = "The type of client (e.g., individual, corporate)", example = "INDIVIDUAL")
    private String clientType ;
    
}
