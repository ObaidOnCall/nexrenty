package ma.crm.carental.entities;


import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.Check;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Entity
@Table(
        name = "payments" ,
        indexes = {
            @Index(columnList = "tenantId" , name = "payment_tenantId_idx")
        }
        
)
@Check(name = "ValidAmount", constraints = "amount > 10")
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor @AllArgsConstructor @Builder
public class Payment extends AbstractBaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "payment_seq")
    @SequenceGenerator(name = "payment_seq" , sequenceName = "payment_id_seq"  , allocationSize = 1 , initialValue = 19999)
    private Long id ;

    @Column(nullable = false)
    private Double amount ;

    @Column(nullable = false ,length = 10)
    private String currency ;

    @Column(nullable = false)
    private ZonedDateTime paymentDate ;

    @Column(length = 20 , nullable = false)
    private String paymentMethod ;


    @CreationTimestamp
    private ZonedDateTime createdAt ;

    @UpdateTimestamp
    private ZonedDateTime updatedAt ;
    
}
