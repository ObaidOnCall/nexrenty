package ma.crm.carental.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import io.minio.ObjectWriteResponse;
import ma.crm.carental.dtos.client.ClientRequestDto;
import ma.crm.carental.dtos.client.ClientResponseDto;
import ma.crm.carental.dtos.docs.FileResponseDto;
import ma.crm.carental.dtos.docs.MetaData;
import ma.crm.carental.entities.Client;
import ma.crm.carental.entities.ClientDocs;



@Component
public class ClientMapper {
    

    public List<Client> toClient(List<ClientRequestDto> clientRequestDtos) {


        return clientRequestDtos.stream()
                                .map(clientRequestDto ->
                                            Client.builder()
                                            .firstname(clientRequestDto.getFirstname())
                                            .lastname(clientRequestDto.getLastname())
                                            .address(clientRequestDto.getAddress())
                                            .email(clientRequestDto.getEmail())
                                            .ville(clientRequestDto.getVille())
                                            .nationality(clientRequestDto.getNationality())
                                            .clientType(clientRequestDto.getClientType())
                                            .cinOrPassport(clientRequestDto.getCinOrPassport())
                                            .cinIsValideUntil(clientRequestDto.getCinIsValideUntil())
                                            .phone1(clientRequestDto.getPhone1())
                                            .phone2(clientRequestDto.getPhone2())
                                            .codePostal(clientRequestDto.getCodePostal())
                                            .build() 
                ).collect(Collectors.toList()) ;

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
