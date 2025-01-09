package ma.crm.carental.dtos.reservation;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.crm.carental.dtos.vehicule.VehResponseDto;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class ReservationResponseDto {
    

    private Long id ;

    private boolean paymentStatus;

    private Date startDate;

    private Date endDate;

    private Double totalPrice;

    private String notes;

    private VehResponseDto vehicule;
}
