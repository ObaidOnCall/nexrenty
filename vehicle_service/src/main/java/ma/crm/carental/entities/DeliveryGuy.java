package ma.crm.carental.entities;


import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
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
        name = "deliveryGuys" ,
        indexes = {
            @Index(columnList = "tenantId" , name = "deliveryGuy_tenantId_idx")
        }
)
@Data  @EqualsAndHashCode(callSuper = false)
@NoArgsConstructor @AllArgsConstructor @Builder
public class DeliveryGuy extends AbstractBaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "deliveryGuy_seq")
    @SequenceGenerator(name = "deliveryGuy_seq" , sequenceName = "deliveryGuy_id_seq"  , allocationSize = 1)
    private Long id ;


    private String firstname ;

    private String lastname ;

    private String cinOrPassport ;

    private String licence ;

    @Column(nullable = false , length = 24)
    private String nationality ;

    @Column(nullable = false)
    private String address ;

    @Column(nullable = false)
    private String ville ;

    @Column
    private int codePostal ;

    @Column(nullable = false)
    private String phone1 ;

    private String phone2 ;

    private String email ;

    private Date cinIsValideUntil ;
    
    private Date licenceIsValideUntil ;

    @OneToMany(mappedBy = "deliveryGuy" , fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Contract> contracts ;
    
}
