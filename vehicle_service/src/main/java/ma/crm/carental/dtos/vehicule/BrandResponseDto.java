package ma.crm.carental.dtos.vehicule;


import java.util.List;

import lombok.Getter;
import lombok.Setter;
import ma.crm.carental.entities.Model;


@Getter @Setter
public class BrandResponseDto {
    
    private Long id ;
    private String name ;

    private String countryOfOrigin ;
    private String parentCompany ;
    private String website ;

}
