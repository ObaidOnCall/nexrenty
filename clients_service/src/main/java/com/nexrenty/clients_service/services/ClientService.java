package com.nexrenty.clients_service.services;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.nexrenty.clients_service.dtos.ClientRequestDto;
import com.nexrenty.clients_service.dtos.ClientResponseDto;
import com.nexrenty.clients_service.dtos.DeleteUpdateResponseDto;
import com.nexrenty.clients_service.entities.Client;
import com.nexrenty.clients_service.exception.UnableToProccessIteamException;
import com.nexrenty.clients_service.mappers.ClientMapper;
import com.nexrenty.clients_service.repositories.ClientRepo;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.SnowballObject;
import io.minio.UploadSnowballObjectsArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.MinioException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.http.Method;
import jakarta.persistence.NoResultException;
import lombok.extern.slf4j.Slf4j;



import org.springframework.data.domain.Page;

@Service
@Transactional
@Slf4j
public class ClientService {
    
    private static final String ERRORMESSAGE = "access denied or unable to process the item within the client" ;



    private final ClientRepo clientRepo ;
    private final ClientMapper clientMapper ;

    @Autowired
    ClientService(
            ClientRepo clientRepo ,
            ClientMapper clientMapper

        ) {
        this.clientRepo = clientRepo ;
        this.clientMapper = clientMapper ;
    }


    public List<ClientResponseDto> saveClients(List<ClientRequestDto> clientRequestDtos){

        return clientMapper.fromClient(
                                clientRepo.insertClientInBatch(
                                            clientMapper.toClient(clientRequestDtos)
                            ));
    }

    public DeleteUpdateResponseDto deleteClients(List<Long> clientsIds) {
        
        int count = clientRepo.deleteClients(clientsIds) ;

        return DeleteUpdateResponseDto.builder()
                                        .status(true)
                                        .message("Number of deleted clients: " + count)
                                        .count(count)
                                        .build() ;
        
    }

    public DeleteUpdateResponseDto updateClients(List<Long> clientsIds , List<ClientRequestDto> clients) {


        int count = clientRepo.updateClientsInBatch(clientsIds, clientMapper.toClient(clients).get(0)) ;

        return DeleteUpdateResponseDto.builder()
                                        .status(true)
                                        .message("Number of updated clients: " + count)
                                        .count(count)
                                        .build() ;
    }



    public Page<ClientResponseDto> pagenateClients(Pageable pageable) {
        
        Long totalElements = clientRepo.count() ;


        List<ClientResponseDto> clientResponseDtos = clientMapper.fromClient(
            clientRepo.clientsWithPagination(pageable.getPageNumber(), pageable.getPageSize())
        ) ;

        return new PageImpl<>(clientResponseDtos , pageable , totalElements) ;


    }

    public ClientResponseDto findClient(long id) {

        
        try {
            Client client = clientRepo.find(id) ;

            /**
             * @convert the client object to List of Client to use the general mapper
             */
            List<Client> clients = List.of(client) ;

            return clientMapper.fromClient(clients).get(0) ;

        } catch (NoResultException e) {
            throw new UnableToProccessIteamException(ClientService.ERRORMESSAGE) ;
        }
    }

    public List<ClientResponseDto> findClients(List<Long> ids ) {
        try {
            
            return clientMapper.fromClient(clientRepo.findClients(ids)) ;

        } catch (NoResultException e) {
            
            throw new UnableToProccessIteamException(ClientService.ERRORMESSAGE) ;

        }
    }

}
