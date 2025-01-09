package ma.crm.carental.dtos.docs;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileResponseDto {
    
    private long id ;

    private String filename ;

    private double size ;

    private String contentType ;

    private String bucket ;


    private Date createAt ;
}
