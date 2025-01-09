package ma.crm.carental.entities;


import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
        name = "billings" ,
        indexes = {
            @Index(columnList = "tenantId" , name = "billing_tenantId_idx")
        },
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"tenantId" , "numBilling"})
        }
)
@Data  @EqualsAndHashCode(callSuper = false)
@NoArgsConstructor @AllArgsConstructor @Builder
public class Billing extends AbstractBaseEntity{
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "billing_seq")
    @SequenceGenerator(name = "billing_seq" , sequenceName = "billing_id_seq"  , allocationSize = 1)
    private Long id ;


    @Column(nullable = false)
    private Long numBilling ;

    @Column(nullable = false , updatable = false)
    private ZonedDateTime dateOfBilling ;


    private String place ;
    
    private Date fromDate ;

    private Date toDate ;

    private Double pricePerDay ;

    private Double durationPerDay ;

    private Double amountHt ;

    private Double amountTTC ;

    @Column(name = "deleted")
    @Builder.Default
    private Boolean deleted = false;


    @Column
    private int tva ;

    private String createdBy ;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
		name = "billings_payments",
		joinColumns = @JoinColumn(name = "billing_fk"),
		inverseJoinColumns = @JoinColumn(name = "payment_fk")
    )
    private List<Payment> payments ;


    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Contract contract ;

    @JoinColumn(nullable=false , name = "client_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Client client ;

}   
