package ma.crm.carental.entities;


import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Entity
@Table(
        name = "reservations" ,
        indexes = {
            @Index(columnList = "tenantId" , name = "reservation_tenantId_idx")
        }
)
@Data  @EqualsAndHashCode(callSuper = false)
@NoArgsConstructor @AllArgsConstructor @Builder
public class Reservation extends AbstractBaseEntity{
    

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "reservation_seq")
    @SequenceGenerator(name = "reservation_seq" , sequenceName = "reservation_id_seq"  , allocationSize = 1 , initialValue = 200)
    private Long id ;

    @Column(name = "isPaid")
    private boolean isPaid;

    

    private Date startDate;

    private Date endDate;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "notes")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "vehicule_id" , nullable = false)
    private Vehicule vehicule;

}
