package ma.crm.carental.dtos.contract;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import ma.crm.carental.dtos.interfaces.ClientIdentifiable;
import ma.crm.carental.dtos.interfaces.DeliveryGuyIdentifiable;
import ma.crm.carental.dtos.interfaces.VehiclueIdentifiable;
import ma.crm.carental.dtos.interfaces.validationgroups.CreateValidationGroup;
import ma.crm.carental.dtos.interfaces.validationgroups.UpdateValidationGroup;


@Data
public class ContractRequestDto implements ClientIdentifiable , VehiclueIdentifiable , DeliveryGuyIdentifiable{
    

    @NotNull(message = "Contract number is required", groups = CreateValidationGroup.class)
    private Long numContract;

    @NotNull(message = "Start date is required", groups = CreateValidationGroup.class)
    private Date startDate;

    @NotNull(message = "End date is required", groups = CreateValidationGroup.class)
    private Date finDate;

    @Positive(message = "Days must be a positive number", groups = CreateValidationGroup.class)
    private int days;

    @NotBlank(message = "Place of contract is required", groups = CreateValidationGroup.class)
    private String placeOfContract;

    @NotBlank(message = "Place of delivery is required", groups = CreateValidationGroup.class)
    private String placeOfDelivery;

    @NotBlank(message = "Place of return is required", groups = CreateValidationGroup.class)
    private String placeOfReturn;

    @Positive(message = "Price must be positive", groups = CreateValidationGroup.class)
    private double price;

    @PositiveOrZero(message = "Start mileage must be positive", groups = { CreateValidationGroup.class, UpdateValidationGroup.class })
    private long startMileage;

    @PositiveOrZero(message = "Caution cannot be negative", groups = { CreateValidationGroup.class, UpdateValidationGroup.class })
    private int caution;

    @PositiveOrZero(message = "Total amount must be positive", groups = { CreateValidationGroup.class, UpdateValidationGroup.class })
    private double totalAmount;

    @PositiveOrZero(message = "Pre-given price cannot be negative", groups = { CreateValidationGroup.class, UpdateValidationGroup.class })
    private double preGivenPrice;

    @PositiveOrZero(message = "Remaining price cannot be negative" , groups = { CreateValidationGroup.class, UpdateValidationGroup.class })
    private Double remainingPrice;

    private Date dateValideCin;
    private Date dateValideDrivingLicence;

    @PositiveOrZero(message = "Delivery costs cannot be negative")
    private Double deliveryCosts;


    @NotNull(message = "Vehicule is required", groups = CreateValidationGroup.class)
    private Long vehicule;

    @NotNull(message = "Client is required", groups = CreateValidationGroup.class)
    private Long client;

    @NotNull(message = "Delivery guy is required", groups = CreateValidationGroup.class)
    private Long deliveryGuy;
}
