package ma.crm.carental.dtos.vehicule;

import java.util.Date;

import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ModelResponseDto {
    
    private long id ;

    private String name ;

    private Date year ;

    private String engineType ;

    private String transmission ;

    private String fuelType ;

    private double length ;

    private double width ;

    private double height ;

    private double weight ;

    private double fuelEfficiency ;

    private int satingCapacity ;

    private int topSpeed ;

    private int numberOfDoors ;

    private BrandResponseDto brand ;
}
