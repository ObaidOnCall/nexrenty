package ma.crm.carental.entities;


import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;



/**
 * @see Billing is Facturation
 */
@Entity
@Table(
        name = "charges" ,
        indexes = {
            @Index(columnList = "tenantId" , name = "charge_tenantId_idx")
        }
)
@Data  @EqualsAndHashCode(callSuper = false)
@NoArgsConstructor @AllArgsConstructor @Builder
public class Charge extends AbstractBaseEntity{
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "charge_seq")
    @SequenceGenerator(name = "charge_seq" , sequenceName = "charge_id_seq"  , allocationSize = 1)
    private Long id ;

    @Column
    private double amount ;

    private String changeType ;

    private String descrption ;

    private Boolean isPaid ;

    @Column(nullable = false)
    private String createdBy ;

    @JoinColumn(nullable=false , name = "contract_id")
    @ManyToOne
    private Contract contract ;

    @OneToMany(mappedBy = "charge")
    private List<Violation> violations ;

    @CreationTimestamp
        private Date createdAt ;

    @UpdateTimestamp
    private Date updatedAt ;
}
