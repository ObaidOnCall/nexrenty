package com.nexrenty.clients_service.dtos;

import com.nexrenty.clients_service.dtos.interfaces.ClientIdentifiable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetaData implements ClientIdentifiable{

    private String bucket ;
    
    private String region ;

    private Long client;
    
}
