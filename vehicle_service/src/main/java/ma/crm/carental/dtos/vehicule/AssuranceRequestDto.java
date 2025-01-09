package ma.crm.carental.dtos.vehicule;

import java.sql.Date;
import java.time.DateTimeException;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssuranceRequestDto {
    
    @NotBlank
    private String type ;

    @NotNull
    private Date date ;

    @NotNull
    private Date expireDate ;

    private long price ;

    private String assuranceContact ;

    private String obervation ;

    @NotNull
    private Long vehicle ;


    void dateChecking() {
        if (date.after(expireDate)) {
            throw new DateTimeException("Experie date must be greater than date .") ;
        }
    }
}
