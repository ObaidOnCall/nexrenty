package ma.crm.carental.entities;


import java.time.ZonedDateTime;
import java.util.ArrayList;
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
        name = "clients" ,
        indexes = {
            @Index(columnList = "tenantId" , name = "client_tenantId_idx")
        },
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"tenantId" , "firstname" , "lastname"})
        }
)
@Data  @EqualsAndHashCode(callSuper = false)
@NoArgsConstructor @AllArgsConstructor @Builder
public class Client extends AbstractBaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "client_seq")
    @SequenceGenerator(name = "client_seq" , sequenceName = "client_id_seq"  , allocationSize = 1)
    private Long id ;


    private String firstname ;

    private String lastname ;

    private String cinOrPassport ;

    private String licence ;

    @Column(nullable = false , length = 255)
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
    // ZonedDateTime
    private Date cinIsValideUntil ;

    // ZonedDateTime
    private Date licenceIsValideUntil ;

    private String clientType ;


    @OneToMany(mappedBy = "client")
    private List<Billing> billings ;

    // @OneToMany(mappedBy = "client")
    // private List<Violation> violations ;

    // @OneToMany(mappedBy = "client")
    // private List<Contract> contracts ;

    @OneToMany(mappedBy = "client")
    private List<ClientDocs> clientDocs ;

    
    
}
