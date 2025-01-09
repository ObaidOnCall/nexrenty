package ma.crm.carental.entities;

import java.time.ZonedDateTime;
import java.util.Date;

import org.aspectj.apache.bcel.generic.InstructionConstants.Clinit;
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
import jakarta.persistence.ManyToOne;
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
        name = "violations" ,
        indexes = {
            @Index(columnList = "tenantId" , name = "violation_tenantId_idx")
        }
)
@Data  @EqualsAndHashCode(callSuper = false)
@NoArgsConstructor @AllArgsConstructor @Builder
public class Violation extends AbstractBaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "violation_seq")
    @SequenceGenerator(name = "violation_seq" , sequenceName = "violation_id_seq"  , allocationSize = 1)
    private Long id ;


    private String description ;

    @Column
    private Double finAmount ;

    private Date date ;

    private Boolean isPaid ;

    // @JoinColumn(nullable = false)
    // @ManyToOne
    // private Client client ;


    @JoinColumn(name = "client_id", nullable = false)
    private Long clientId;


    @JoinColumn(name = "charge_id" , nullable = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private Charge charge ;

    @CreationTimestamp
    private Date createdAt ;

    @UpdateTimestamp
    private Date updatedAt ;
    
}
