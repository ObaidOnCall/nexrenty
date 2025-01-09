package ma.crm.carental.dtos.docs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.crm.carental.dtos.interfaces.ClientIdentifiable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetaData implements ClientIdentifiable{

    private String bucket ;
    
    private String region ;

    private Long client;
    
}
