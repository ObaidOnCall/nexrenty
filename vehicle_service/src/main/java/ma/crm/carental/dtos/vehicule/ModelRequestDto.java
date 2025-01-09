package ma.crm.carental.dtos.vehicule;

import java.util.Date;

import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class ModelRequestDto implements ModelDtoInterface{
    
    @NotBlank(message = "Name must not be blank")
    @Size(min = 2, max = 30, message = "Name must be between 2 and 30 characters")
    private String name;

    @PastOrPresent(message = "Year must be in the past or present")
    private Date year;

    @Size(min = 5, max = 15, message = "Engine Type must be between 5 and 15 characters")
    private String engineType;

    @Size(min = 5, max = 15, message = "Transmission must be between 5 and 15 characters")
    private String transmission;

    @Size(min = 5, max = 15, message = "Fuel Type must be between 5 and 15 characters")
    private String fuelType;

    @DecimalMin(value = "0.5", message = "Length must be between 0.5 and 3 meters")
    @DecimalMax(value = "3.0", message = "Length must be between 0.5 and 3 meters")
    private double length;

    @DecimalMin(value = "0.5", message = "Width must be between 0.5 and 3 meters")
    @DecimalMax(value = "3.0", message = "Width must be between 0.5 and 3 meters")
    private double width;

    @DecimalMin(value = "0.5", message = "Height must be between 0.5 and 3 meters")
    @DecimalMax(value = "3.0", message = "Height must be between 0.5 and 3 meters")
    private double height;

    @DecimalMin(value = "0.5", message = "Weight must be between 0.5 and 3 tons")
    @DecimalMax(value = "3.0", message = "Weight must be between 0.5 and 3 tons")
    private double weight;

    @Range(min = 5, max = 10, message = "Fuel Efficiency per 100km must be between 5 and 10 liters")
    private double fuelEfficiency;

    @Range(min = 1, max = 8, message = "Seating Capacity must be between 1 and 8")
    private int seatingCapacity;

    @NotNull(message = "Top Speed cannot be null")
    @Range(min = 80, max = 299, message = "Top Speed must be between 80 and 299 km/h")
    private int topSpeed;

    @NotNull(message = "Number of Doors cannot be null")
    @Range(min = 2, max = 8, message = "Number of Doors must be between 2 and 8")
    private int numberOfDoors;

    @NotNull(message = "The Model must have a Brand")
    private Long brand;

}
