package ma.crm.carental.dtos.violation;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.crm.carental.dtos.client.ClientResponseDto;


@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ViolationResponseDto {


    private Long id ;


    private String description ;

    private double finAmount ;

    private Date date ;

    private Boolean isPaid ;

    private long client ;

    // @JoinColumn(name = "charge_id" , nullable = false)
    // @ManyToOne(fetch = FetchType.LAZY)
    // private Charge charge ;
    
}
