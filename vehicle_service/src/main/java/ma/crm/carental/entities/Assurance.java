    package ma.crm.carental.entities;

    import java.sql.Date;
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
    import jakarta.persistence.SequenceGenerator;
    import jakarta.persistence.Table;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.NoArgsConstructor;

    @Entity
    @Table(
        name = "assurances" ,
        indexes = {
            @Index(columnList = "tenantId" , name = "assurance_tenantId_idx") ,
        }
    )
    @Data
    @EqualsAndHashCode(callSuper = false)
    @NoArgsConstructor 
    @AllArgsConstructor 
    @Builder
    public class Assurance extends AbstractBaseEntity{
        
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "assurance_seq")
        @SequenceGenerator(name = "assurance_seq" , sequenceName = "assurance_id_seq"  , allocationSize = 20)
        private Long id ;

        @Column(length = 32)
        private String type ;

        @Column(nullable = false)
        private Date date ;

        @Column(nullable = false)
        private Date expireDate ;

        @Column(nullable = false)
        private long price ;

        private String assuranceContact ;

        private String obervation ;


        @CreationTimestamp
        private Date createdAt ;

        @UpdateTimestamp
        private Date updatedAt ;

        @ManyToOne(optional = false ,fetch = FetchType.LAZY)
        @JoinColumn(name = "vehicule_id" , nullable = false)
        private Vehicule vehicule ;
    }
