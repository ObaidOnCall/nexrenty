package ma.crm.carental.entities;

import java.util.Date;

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
        name = "clientdocs" ,
        indexes = {
            @Index(columnList = "tenantId" , name = "clientdoc_tenantId_idx")
        },
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"tenantId" , "filename"})
        }
)
@Data  @EqualsAndHashCode(callSuper = false)
@NoArgsConstructor @AllArgsConstructor @Builder
public class ClientDocs extends AbstractBaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "clientdoc_seq")
    @SequenceGenerator(name = "clientdoc_seq" , sequenceName = "clientdoc_id_seq"  , allocationSize = 1)
    private Long id ;

    @Column(nullable = false)
    private String filename ;

    @Column(nullable = false)
    private double size ;

    @Column(nullable = false)
    private String contentType ;

    @Column
    private String bucket ;

    @Column
    private String region ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Client client ;


    @CreationTimestamp
    private Date createdAt ;

    @UpdateTimestamp
    private Date updatedAt ;
}
