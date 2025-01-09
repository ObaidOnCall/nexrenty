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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "subscriptiontypes" ,
        indexes = {
            @Index(columnList = "name" , name = "subscriptiontype_name_idx")
        }
)
@Data
@NoArgsConstructor @AllArgsConstructor @Builder
public class SubscriptionType {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String name; // e.g., "Pro", "Enterprise"

    @Column
    private String description; // Detailed description of the subscription tier

    @Column(nullable = false)
    private BigDecimal price; // Price for this subscription tier

    // Limits for each type of resource
    @Column(name = "max_vehicles", nullable = false)
    private int maxVehicles;

    @Column(name = "max_clients", nullable = false)
    private int maxClients;

    @Column(name = "max_brands", nullable = false)
    private int maxBrands;

    @Column(name = "max_charges", nullable = false)
    private int maxCharges;

    @Column(name = "max_contracts", nullable = false)
    private int maxContracts;

    @Column(name = "max_deliveries", nullable = false)
    private int maxDeliveries;

    @Column(name = "max_reservations", nullable = false)
    private int maxReservations;

    @Column(name = "max_violations", nullable = false)
    private int maxViolations;

    @Column(name = "duration_months", nullable = false)
    private int durationMonths; // Duration of the subscription in months


    @CreationTimestamp
    private Date createdAt ;

    @UpdateTimestamp
    private Date updatedAt ;
    
}
