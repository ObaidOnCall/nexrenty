package ma.crm.carental.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "subscriptions" ,
        indexes = {
            @Index(columnList = "tenantId" , name = "subscription_tenantId_idx")
        },
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"tenantId" , "subscriptionType"})
        }
)
@Data @EqualsAndHashCode(callSuper = false)
@NoArgsConstructor @AllArgsConstructor @Builder
public class Subscription extends AbstractBaseEntity{

    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(nullable = false)
    SubscriptionType subscriptionType ;


    @CreationTimestamp
    private Date createdAt ;

    @UpdateTimestamp
    private Date updatedAt ;
    
}
