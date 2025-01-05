package com.nexrenty.clients_service.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.nexrenty.clients_service.dtos.ClientResponseDto;
import com.nexrenty.clients_service.dtos.FileResponseDto;
import com.nexrenty.clients_service.dtos.MetaData;
import com.nexrenty.clients_service.dtos.PresignedURL;
import com.nexrenty.clients_service.entities.Client;
import com.nexrenty.clients_service.entities.ClientDocs;
import com.nexrenty.clients_service.exception.UnableToProccessIteamException;
import com.nexrenty.clients_service.mappers.ClientDocMapper;
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
import io.minio.http.Method;

import io.minio.errors.XmlParserException;
import jakarta.persistence.NoResultException;


@Service
@Transactional
public class ClientDocService {

    private static final String CLIENT_NOT_FOUND = "access denied or unable to process the item within the client" ;

    
    private final ClientRepo clientRepo ;
    private final ClientDocMapper clientDocMapper ;
    private final ClientMapper clientMapper ; 
    private final MinioClient minioClient ;


    @Autowired
    ClientDocService(
        ClientRepo clientRepo ,
        ClientDocMapper clientDocMapper , 
        ClientMapper clientMapper ,
        MinioClient minioClient

    ) {
        this.clientRepo = clientRepo ;
        this.clientDocMapper = clientDocMapper ;
        this.clientMapper = clientMapper ;
        this.minioClient = minioClient ;
    }

    public List<FileResponseDto>  upload (
        List<MetaData> docsMetaData ,
        List<MultipartFile> files
    ) throws MinioException, IOException, InvalidKeyException, NoSuchAlgorithmException, IllegalArgumentException{


        ClientResponseDto client = this.findClient(docsMetaData.get(0).getClient()) ;


        List<SnowballObject> snowballObjects = new ArrayList<>();

        for (MultipartFile file : files) {
            
            SnowballObject snowballObject = new SnowballObject(
                client.getFirstname()+ "__" +file.getOriginalFilename(),
                new ByteArrayInputStream(file.getBytes()),
                file.getSize(),
                null // Optionally, you can provide compression methods here
            );
            snowballObjects.add(snowballObject);
        }

        ObjectWriteResponse minioResponse = minioClient.uploadSnowballObjects(
                                                UploadSnowballObjectsArgs.builder()
                                                                        .bucket("clients")
                                                                        .objects(snowballObjects)
                                                                        .build()
                                        );
        MetaData metaData = MetaData.builder()
                            .client(docsMetaData.get(0).getClient())
                            .bucket(minioResponse.bucket())
                            .region(minioResponse.region())
                            .build() ;
        
        List<ClientDocs> clientDocs =  clientDocMapper.toClientDocs(files, metaData , client) ;
        clientDocs = clientRepo.insertClientDocs(clientDocs) ;

        return clientDocMapper.fromClientDocs(clientDocs) ;
    }


    public List<FileResponseDto> listFiles (long id) {

        try {
            Client client = clientRepo.find(id) ;

            return clientDocMapper.fromClientDocs(
                client.getClientDocs()
            );

        } catch (NoResultException e) {
            throw new UnableToProccessIteamException(CLIENT_NOT_FOUND) ;
        }
    }



    public PresignedURL presignedUrl(long id) throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException, XmlParserException, ServerException, IllegalArgumentException, IOException{

        int expiryDuration = 2;

        try {

            ClientDocs clientDocs = clientRepo.findClientDocs(id) ;

            String url = minioClient.getPresignedObjectUrl(
                            GetPresignedObjectUrlArgs.builder()
                                .method(Method.GET)
                                .bucket("clients")
                                .object(clientDocs.getFilename())
                                .expiry(expiryDuration, TimeUnit.MINUTES)
                                .build());

            Map<String, Object> expirationDetails = new HashMap<>();
            expirationDetails.put("duration", expiryDuration);
            expirationDetails.put("unit", "MINUTES");

            return PresignedURL.builder()
                                .url(url)
                                .expiration(expirationDetails)
                                .build() ;

        } catch (NoResultException e) {
            throw new UnableToProccessIteamException(CLIENT_NOT_FOUND) ;
        }

    }


    private  ClientResponseDto findClient(long id) {
        
        try {
            Client client = clientRepo.find(id) ;


            List<Client> clients = List.of(client) ;

            return clientMapper.fromClient(clients).get(0) ;

        } catch (NoResultException e) {
            throw new UnableToProccessIteamException(CLIENT_NOT_FOUND) ;
        }
    }
}
