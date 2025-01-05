package com.nexrenty.clients_service.dtos;


import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nexrenty.clients_service.dtos.interfaces.CreateValidationGroup;
import com.nexrenty.clients_service.dtos.interfaces.UpdateValidationGroup;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
@Schema(description = "Request DTO for client data")
public class ClientRequestDto{
    
    @NotBlank(message = "Firstname is required", groups = CreateValidationGroup.class)
    @Size(min = 2, max = 40, message = "Firstname must be between 2 and 40 characters" , groups = { CreateValidationGroup.class, UpdateValidationGroup.class })
    @Pattern(regexp = "^[A-Za-z]+$", message = "Firstname must only contain alphabetic characters" , groups = { CreateValidationGroup.class, UpdateValidationGroup.class })
    @Schema(description = "The first name of the client", example = "John")
    private String firstname;

    @NotBlank(message = "LastName is required", groups = CreateValidationGroup.class)
    @Size(min = 2, max = 30, message = "LastName must be between 2 and 30 characters", groups = { CreateValidationGroup.class, UpdateValidationGroup.class })
    @Pattern(regexp = "^[A-Za-z]+$", message = "LastName must only contain alphabetic characters", groups = { CreateValidationGroup.class, UpdateValidationGroup.class })
    @Schema(description = "The last name of the client", example = "Doe")
    private String lastname;

    @NotBlank(message = "Cin/Passport is required", groups = CreateValidationGroup.class)
    @Size(min = 8, max = 20, message = "Cin/Passport must be between 8 and 20 characters", groups = { CreateValidationGroup.class, UpdateValidationGroup.class })
    @Schema(description = "The CIN (National ID) or passport number of the client", example = "AB123456") 
    private String cinOrPassport;

    @NotBlank(message = "Licence is required", groups = CreateValidationGroup.class)
    @Size(min = 4, max = 20, message = "Licence must be between 4 and 20 characters", groups = { CreateValidationGroup.class, UpdateValidationGroup.class })
    @Schema(description = "The driving licence number of the client", example = "LIC123456")
    private String licence;

    @Size(min = 4, max = 40, message = "Nationality must be between 4 and 40 characters", groups = { CreateValidationGroup.class, UpdateValidationGroup.class })
    @Schema(description = "The nationality of the client", example = "American")    
    private String nationality;

    @Size(min = 8, max = 50, message = "Address must be between 8 and 50 characters", groups = { CreateValidationGroup.class, UpdateValidationGroup.class })
    @Schema(description = "The address of the client", example = "123 Main St")
    private String address;

    @Size(max = 50, message = "City name should not exceed 50 characters", groups = { CreateValidationGroup.class, UpdateValidationGroup.class })
    @Schema(description = "The city of residence of the client", example = "New York")
    private String ville;

    @Schema(description = "The postal code of the client's address", example = "10001")
    private int codePostal;

    @Pattern(
        regexp = "^\\+(?:\\d-?){1,20}\\d$",
        message = "Phone number must start with + and contain up to 20 digits (hyphens are optional)",
        groups = { CreateValidationGroup.class, UpdateValidationGroup.class }
    )
    @Schema(description = "The primary phone number of the client", example = "+1234567890")
    private String phone1;

    @Pattern(
        regexp = "^\\+(?:\\d-?){1,20}\\d$",
        message = "Phone number must start with + and contain up to 20 digits (hyphens are optional)",
        groups = { CreateValidationGroup.class, UpdateValidationGroup.class }
    )
    @Schema(description = "The secondary phone number of the client", example = "+0987654321")
    private String phone2;

    @Email(message = "Please provide a valid email address", groups = { CreateValidationGroup.class, UpdateValidationGroup.class })
    @Schema(description = "The expiration date of the client's CIN or passport", example = "2025-12-31T23:59:59Z")
    private String email;

    @Schema(description = "The expiration date of the client's CIN or passport", example = "2025-12-31T23:59:59Z")
    private Instant cinIsValideUntil;

    @Schema(description = "The expiration date of the client's driving licence", example = "2025-12-31T23:59:59Z")
    private Instant licenceIsValideUntil;

    @Schema(description = "The type of client (e.g., individual, corporate)", example = "INDIVIDUAL")
    private String clientType;
}
