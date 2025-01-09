package ma.crm.carental.dtos.charge;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import ma.crm.carental.dtos.interfaces.ContractIdentifiable;
import ma.crm.carental.dtos.interfaces.validationgroups.CreateValidationGroup;
import ma.crm.carental.dtos.interfaces.validationgroups.UpdateValidationGroup;


@Data
public class ChargeRequestDto implements ContractIdentifiable{
    
    @NotNull(message = "Amount is required", groups = CreateValidationGroup.class)
    @PositiveOrZero(message = "Amount must be positive", groups = { CreateValidationGroup.class, UpdateValidationGroup.class })
    private double amount;

    @NotBlank(message = "Change type is required", groups = CreateValidationGroup.class)
    private String changeType;

    private String description;

    @NotNull(message = "Payment status is required", groups = CreateValidationGroup.class)
    private Boolean isPaid;

    @NotNull(message = "Contract ID is required", groups = CreateValidationGroup.class)
    private Long contract;
}
