package ma.crm.carental.dtos.subscription;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.crm.carental.entities.SubscriptionType;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class SubscriptionResponseDto {
    

    private String id;

    SubscriptionType subscriptionType ;


    private Date createdAt ;

    private Date updatedAt ;
}
