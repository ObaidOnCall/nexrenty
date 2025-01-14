package ma.crm.carental.entities;

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

@Entity
@Table(
        name = "contracts" ,
        indexes = {
            @Index(columnList = "tenantId" , name = "contract_tenantId_idx")
        },
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"tenantId" , "numContract"})
        }
)
@Data  @EqualsAndHashCode(callSuper = false)
@NoArgsConstructor @AllArgsConstructor @Builder
public class Contract extends AbstractBaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "contract_seq")
    @SequenceGenerator(name = "contract_seq" , sequenceName = "contract_id_seq"  , allocationSize = 1 , initialValue = 19999)
    private Long id ;

    @Column(nullable = false)
    private Long numContract ;
    
    @Column(nullable = false)
    private Date startDate ;

    @Column(nullable = false)
    private Date finDate ;

    @Column(nullable = false)
    private int days ;

    @Column(nullable = false , updatable = false)
    private String placeOfContract ;

    private String placeOfDelivery ;

    private String placeOfReturn ;

    @Column(nullable = false )
    private double price ;

    private long startMileage ;

    @Column(nullable = true)
    private int caution ;

    @Column(nullable = false)
    private double totalAmount ;

    private double preGivenPrice ;

    @Column(nullable = false)
    private Double remainingPrice ;

    @Column
    private Date dateValideCin ;

    @Column
    private Date dateValideDrivingLicence ;

    private Double deliveryCosts ;

    @Column(nullable = false)
    private String createdBy ;

    // @OneToMany(mappedBy = "contract")
    // private List<Billing> billings;
    
    @OneToMany(mappedBy = "contract")
    private List<Charge> charges ;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Vehicule vehicule;

    @JoinColumn(name = "client_id", nullable = false)
    private Long clientId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deliveryGuy_id" , nullable = false)
    private DeliveryGuy deliveryGuy ;



    

    @CreationTimestamp
    private Date createdAt ;

    @UpdateTimestamp
    private Date updatedAt ;


}
