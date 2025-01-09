package ma.crm.carental.entities;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
    name = "models" ,
    indexes = {
            @Index(columnList = "tenantId" , name = "model_tenantId_idx")
    },
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"tenantId" , "name"})
    }
)
@Data @EqualsAndHashCode(callSuper = false)
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Model extends AbstractBaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "model_seq")
    @SequenceGenerator(name = "model_seq" , sequenceName = "model_id_seq"  , allocationSize = 20)
    private Long id ;

    @Column(nullable = false)
    private String name ;
    private Date year ;
    private String engineType ;
    private String transmission ;
    private String fuelType ;
    
    @Column
    private double length ;
    @Column
    private double width ;
    @Column
    private double height ;
    @Column
    private double weight ;

    /**
     * @see Miles per gallon (MPG) or liters per 100 km (L/100 km).
     */
    @Column
    private double fuelEfficiency ;

    /**
     * @see The number of passengers the vehicle can seat (e.g., 5-seater).
     */
    @Column
    private int satingCapacity ;

    @Column(nullable = false)
    private int topSpeed ;

    @Column(nullable = false)
    private int numberOfDoors ;

    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonManagedReference
    private Brand brand ;

    @OneToMany(mappedBy = "model")
    @JsonBackReference
    private List<Vehicule> vehicules ;
}
