package com.nexrenty.clients_service.mappers;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.nexrenty.clients_service.dtos.ClientResponseDto;
import com.nexrenty.clients_service.dtos.FileResponseDto;
import com.nexrenty.clients_service.dtos.MetaData;
import com.nexrenty.clients_service.entities.Client;
import com.nexrenty.clients_service.entities.ClientDocs;

@Component
public class ClientDocMapper {
    

    public List<FileResponseDto> fromClientDocs(List<ClientDocs> clientDocs) {

        return clientDocs.stream().map(
            clientDoc -> FileResponseDto.builder()
                            .id(clientDoc.getId())
                            .filename(clientDoc.getFilename())
                            .size(clientDoc.getSize())
                            .contentType(clientDoc.getContentType())
                            .bucket(clientDoc.getBucket())
                            .createAt(clientDoc.getCreatedAt())
                        .build()
        ).toList() ;
    }


    public List<ClientDocs> toClientDocs(
        List<MultipartFile> files , 
        MetaData metaData ,
        ClientResponseDto client
    ) {

        return files.stream().map(
            file -> ClientDocs.builder()
                            .client(
                                Client.builder()
                                .id(metaData.getClient())
                                .build()
                            )
                            .filename(client.getFirstname()+ "__" +file.getOriginalFilename())
                            .size(file.getSize())
                            .contentType(file.getContentType())
                            .bucket(metaData.getBucket())
                            .region(metaData.getRegion())
                            .build()

        ).toList() ;
    }
}
