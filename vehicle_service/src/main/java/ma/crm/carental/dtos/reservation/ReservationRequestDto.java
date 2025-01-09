package ma.crm.carental.dtos.reservation;

import java.util.Date;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import ma.crm.carental.dtos.interfaces.VehiclueIdentifiable;
import ma.crm.carental.dtos.interfaces.validationgroups.CreateValidationGroup;
import ma.crm.carental.dtos.interfaces.validationgroups.UpdateValidationGroup;

@Data
public class ReservationRequestDto implements VehiclueIdentifiable{
    

    @NotNull(message = "Start date is required", groups = CreateValidationGroup.class)
    private Date startDate;

    @NotNull(message = "End date is required", groups = CreateValidationGroup.class)
    private Date endDate;

    @NotNull(message = "Total price is required", groups = CreateValidationGroup.class)
    @PositiveOrZero(message = "Total price must be positive or zero", groups = { CreateValidationGroup.class, UpdateValidationGroup.class })
    private Double totalPrice;

    private String notes;

    @NotNull(message = "Vehicule is required", groups = CreateValidationGroup.class)
    @Positive(message = "Must be vehicule id", groups = { CreateValidationGroup.class, UpdateValidationGroup.class })
    private Long vehicule;
}
