package ma.crm.carental.dtos.vehicule;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.validator.constraints.Range;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import ma.crm.carental.entities.Brand;
import ma.crm.carental.entities.Model;

// 🛵
@Getter @Setter
public class VehRequsetDto {

    @NotBlank(message = "Every vehicle should have a matricule.")
    private String matricule;

    @Size(max = 255, message = "Description can be up to 255 characters.")
    private String description;

    @NotNull(message = "Model ID is required.")
    private Long model;

    @Size(max = 30, message = "Color name should be up to 30 characters.")
    private String color;

    @Min(value = 0, message = "Mileage cannot be negative.")
    private int mileage;

    @PastOrPresent(message = "Year should be in the past or present.")
    private Date year;

    @NotNull(message = "Price is required.")
    @Range(min = 200, max = 10000, message = "Price must be between 200 and 10,000.")
    private double price;

}
