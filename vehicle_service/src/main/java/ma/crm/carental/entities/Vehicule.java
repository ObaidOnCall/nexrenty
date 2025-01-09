package ma.crm.carental.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
import ma.crm.carental.utils.JsonConverter;

@Entity
@Table(
        name = "vehicules" ,
        indexes = {
            @Index(columnList = "tenantId" , name = "vehicule_tenantId_idx")
        },
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"tenantId" , "matricule"})
        }
)
@Data  @EqualsAndHashCode(callSuper = false)
@NoArgsConstructor @AllArgsConstructor @Builder
public class Vehicule extends AbstractBaseEntity{
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "vhicule_seq")
    @SequenceGenerator(name = "vhicule_seq" , sequenceName = "vhicule_id_seq"  , allocationSize = 1)
    private Long id ;

    @Column(nullable = false)
    private String matricule ;

    // @ManyToOne(fetch = FetchType.EAGER)
    // @JoinColumn(nullable = false)
    // private Brand brand ;

    
    private String color ;
    
    @Column(nullable = false)
    private int mileage ;
    
    @Column(nullable = false)
    private double price ;

    @Column(length = 255)
    private String description ;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    @JsonManagedReference
    private Model model ;

    @OneToMany(mappedBy = "vehicule" , fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Assurance> assurances ;

    @OneToMany(mappedBy = "vehicule" , fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Contract> contracts ;

    // @Column(columnDefinition = "json")
    // @Convert(converter = JsonConverter.class)
    // private Map<String , Serializable> metadata;

    @CreationTimestamp
    private Date createdAt ;

    @UpdateTimestamp
    private Date updatedAt ;

}
