package ma.crm.carental.dtos.docs;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PresignedURL {
    
    private String url ;

    private Map<String, Object> expiration ;
    
}
