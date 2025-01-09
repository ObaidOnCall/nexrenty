package ma.crm.carental.dtos.vehicule;

import java.util.Date;
import java.util.Optional;

import org.hibernate.validator.constraints.Range;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class ModelUpdateRequestDto implements ModelDtoInterface{
    
    @Size(min = 5 , max =  30)
    private  String name ;
    
    private Date year ;


    @Size(min = 5 , max =  15)
    private String engineType ;

    @Size(min = 5 , max =  15)
    private String transmission ;

    @Size(min = 5 , max =  15)
    private String fuelType ;

    @Range(min = 0 , max = 3 , message = "must be between 2 and 3")
    private double length ;

    @Range(min = 0 , max = 3 , message = "must be between 2 and 3")
    private double width ;

    @Range(min = 0 , max = 3 , message = "must be between 2 and 3")
    private double height ;

    @Range(min = 0 , max = 3 , message = "must be between 2 and 3")
    private double weight ;

    @Range(min = 0 , max = 10 , message = "fuel Effeici per 100ks must be between 5 and 10")
    private double fuelEfficiency ;

    private int satingCapacity ;

    @Range(min = 80 , max = 299)
    private int topSpeed ;

    

    private @Range(min= 0, max = 8) int numberOfDoors;

    private Long brand ;



    public boolean isParamsLowPriorityPresent(){

        return getFuelType() != null || 
           getFuelEfficiency() != 0.0 || 
           getTransmission() != null || 
           getEngineType() != null;
    }


    public boolean isParamsHightPriorityPresent() {
        return getBrand() != null ||
                getName() != null ||
                getTopSpeed() != 0 ||
                getNumberOfDoors() != 0 ;
    }
}
