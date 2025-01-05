package com.nexrenty.clients_service.mappers;


import java.util.List;

import org.springframework.stereotype.Component;

import com.nexrenty.clients_service.dtos.ClientRequestDto;
import com.nexrenty.clients_service.dtos.ClientResponseDto;
import com.nexrenty.clients_service.entities.Client;





@Component
public class ClientMapper {
    

    public List<Client> toClient(List<ClientRequestDto> clientRequestDtos) {


        return clientRequestDtos.stream()
                                .map(clientRequestDto ->
                                            Client.builder()
                                            .firstname(clientRequestDto.getFirstname())
                                            .lastname(clientRequestDto.getLastname())
                                            .licence(clientRequestDto.getLicence())
                                            .address(clientRequestDto.getAddress())
                                            .email(clientRequestDto.getEmail())
                                            .ville(clientRequestDto.getVille())
                                            .nationality(clientRequestDto.getNationality())
                                            .clientType(clientRequestDto.getClientType())
                                            .cinOrPassport(clientRequestDto.getCinOrPassport())
                                            .cinIsValideUntil(clientRequestDto.getCinIsValideUntil())
                                            .licenceIsValideUntil(clientRequestDto.getLicenceIsValideUntil())
                                            .phone1(clientRequestDto.getPhone1())
                                            .phone2(clientRequestDto.getPhone2())
                                            .codePostal(clientRequestDto.getCodePostal())
                                            .build() 
                ).toList() ;

    }


    public List<ClientResponseDto> fromClient(List<Client> clients) {

        return clients.stream().map(
            client ->ClientResponseDto.builder()
                                    .id(client.getId())
                                    .firstname(client.getFirstname())
                                    .lastname(client.getLastname())
                                    .email(client.getEmail())
                                    .codePostal(client.getCodePostal())
                                    .address(client.getAddress())
                                    .clientType(client.getClientType())
                                    .cinOrPassport(client.getCinOrPassport())
                                    .cinIsValideUntil(client.getCinIsValideUntil())
                                    .licence(client.getLicence())
                                    .licenceIsValideUntil(client.getLicenceIsValideUntil())
                                    .ville(client.getVille())
                                    .phone1(client.getPhone1())
                                    .phone2(client.getPhone2())
                                    .build()
        ).toList() ;
    }



    // public List<FileResponseDto> fromClientDocs(List<ClientDocs> clientDocs) {

    //     return clientDocs.stream().map(
    //         clientDoc -> FileResponseDto.builder()
    //                         .id(clientDoc.getId())
    //                         .filename(clientDoc.getFilename())
    //                         .size(clientDoc.getSize())
    //                         .contentType(clientDoc.getContentType())
    //                         .bucket(clientDoc.getBucket())
    //                         .createAt(clientDoc.getCreatedAt())
    //                     .build()
    //     ).toList() ;
    // }


    // public List<ClientDocs> toClientDocs(
    //     List<MultipartFile> files , 
    //     MetaData metaData ,
    //     ClientResponseDto client
    // ) {

    //     return files.stream().map(
    //         file -> ClientDocs.builder()
    //                             .client(
    //                                 Client.builder()
    //                                 .id(metaData.getClient())
    //                                 .build()
    //                             )
    //                             .filename(client.getFirstname()+ "__" +file.getOriginalFilename())
    //                             .size(file.getSize())
    //                             .contentType(file.getContentType())
    //                             .bucket(metaData.getBucket())
    //                             .region(metaData.getRegion())
    //                             .build()

    //     ).toList() ;
    // }
}
