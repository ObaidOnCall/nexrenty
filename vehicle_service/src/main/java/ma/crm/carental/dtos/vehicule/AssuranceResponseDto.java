package ma.crm.carental.dtos.vehicule;

import java.sql.Date;


import lombok.Data;

@Data
public class AssuranceResponseDto {
    
    private Long id ;
    private String type ;

    private Date date ;

    private Date expireDate ;

    private long price ;

    private String assuranceContact ;

    private String obervation ;

    private VehResponseDto vehResponseDto ;
}
