package ma.crm.carental.dtos.charge;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.crm.carental.dtos.contract.ContractResponseDto;


@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class ChargeResponseDto {


    private Long id ;

    private double amount ;

    private String changeType ;

    private String descrption ;

    private Boolean isPaid ;

    private ContractResponseDto contract ;

    private String createdBy ;
    
    private Date createdAt ;

    private Date updatedAt ;
}
