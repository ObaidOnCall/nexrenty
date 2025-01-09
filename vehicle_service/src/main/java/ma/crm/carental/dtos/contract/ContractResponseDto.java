package ma.crm.carental.dtos.contract;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.crm.carental.dtos.client.ClientResponseDto;
import ma.crm.carental.dtos.deliveryguy.DeliveryGuyResponseDto;
import ma.crm.carental.dtos.vehicule.VehResponseDto;


@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class ContractResponseDto {

    private Long id ;

    private Long numContract ;
    
    private Date startDate ;

    private Date finDate ;

    private int days ;

    private String placeOfContract ;

    private String placeOfDelivery ;

    private String placeOfReturn ;

    private double price ;

    private long startMileage ;

    private int caution ;

    private double totalAmount ;

    private double preGivenPrice ;

    private Double remainingPrice ;

    private Date dateValideCin ;

    private Date dateValideDrivingLicence ;

    private Double deliveryCosts ;

    private String createdBy ;

    private VehResponseDto vehicule;

    // private ClientResponseDto client ;

    private long client ;
    
    private DeliveryGuyResponseDto deliveryGuy ;

    @CreationTimestamp
    private Date createdAt ;

    @UpdateTimestamp
    private Date updatedAt ;
    
}
