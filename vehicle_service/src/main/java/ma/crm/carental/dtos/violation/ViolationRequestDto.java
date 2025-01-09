package ma.crm.carental.dtos.violation;

import java.util.Date;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ma.crm.carental.dtos.interfaces.ClientIdentifiable;
import ma.crm.carental.dtos.interfaces.validationgroups.CreateValidationGroup;


@Data
public class ViolationRequestDto implements ClientIdentifiable{
    
    @Size(max = 255, message = "Description can be up to 255 characters.")
    private String description;

    @PositiveOrZero(message = "Fine amount must be zero or positive.")
    @NotNull(message = "Fine amount is required for creation.", groups = CreateValidationGroup.class)
    private Double finAmount;

    @PastOrPresent(message = "Date must be in the past or present.")
    private Date date;

    private boolean isPaid;

    @NotNull(message = "Client ID is required for creation.", groups = CreateValidationGroup.class)
    private Long client;

    // @NotNull(message = "Charge ID is required.")
    private Long charge;
}
