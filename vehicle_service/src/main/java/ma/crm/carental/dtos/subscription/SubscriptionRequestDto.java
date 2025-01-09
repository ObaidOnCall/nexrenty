package ma.crm.carental.dtos.subscription;

import java.util.UUID;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ma.crm.carental.dtos.interfaces.validationgroups.CreateValidationGroup;

@Data
public class SubscriptionRequestDto {

    @NotNull(message = "Subscription type is required", groups = CreateValidationGroup.class)
    private UUID subscriptionTypeId;
}
