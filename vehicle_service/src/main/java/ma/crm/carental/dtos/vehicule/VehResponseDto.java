package ma.crm.carental.dtos.vehicule;


import java.io.Serializable;
import java.util.Map;

import ch.qos.logback.core.model.Model;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ma.crm.carental.entities.Brand;


@Setter @Getter @Builder
public class VehResponseDto {
    
    private Long id ;

    private String matricule ;

    private String description;
    // private Brand brand ;

    private ModelResponseDto model ;

    private String color ;

    private int mileage ;

    @NotNull
    private double price ;

    private Map<String , Serializable> metadata;
}
