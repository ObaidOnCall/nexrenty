package ma.crm.carental.services;

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
import ma.crm.carental.annotations.ValidateClients;
import ma.crm.carental.dtos.client.ClientRequestDto;
import ma.crm.carental.dtos.client.ClientResponseDto;
import ma.crm.carental.dtos.docs.FileResponseDto;
import ma.crm.carental.dtos.docs.MetaData;
import ma.crm.carental.dtos.docs.PresignedURL;
import ma.crm.carental.entities.Client;
import ma.crm.carental.entities.ClientDocs;
import ma.crm.carental.exception.UnableToProccessIteamException;
import ma.crm.carental.mappers.ClientMapper;
import ma.crm.carental.repositories.ClientRepo;


import org.springframework.data.domain.Page;

@Service
@Transactional
@Slf4j
public class ClientService {
    
    private static final String ERRORMESSAGE = "access denied or unable to process the item within the client" ;



    private final ClientRepo clientRepo ;
    private final ClientMapper clientMapper ;
    private final MinioClient minioClient ;

    @Autowired
    ClientService(
            ClientRepo clientRepo ,
            ClientMapper clientMapper ,
            MinioClient minioClient

        ) {
        this.clientRepo = clientRepo ;
        this.clientMapper = clientMapper ;
        this.minioClient = minioClient ;
    }


    public List<ClientResponseDto> saveClients(List<ClientRequestDto> clientRequestDtos){

        return clientMapper.fromClient(
                                clientRepo.insertClientInBatch(
                                            clientMapper.toClient(clientRequestDtos)
                            ));
    }

    public Map<String , Object> deleteClients(List<Long> clientsIds) {

        Map<String , Object> serviceMessage = new HashMap<>() ;
        
        int count = clientRepo.deleteClients(clientsIds) ;

        serviceMessage.put("status", true) ;
        serviceMessage.put("message", "Number Of Deleted Client is :" + count) ;

        return serviceMessage ;
        
    }

    public Map<String , Object> updateClients(List<Long> clientsIds , List<ClientRequestDto> clients) {

        Map<String , Object> serviceMessage = new HashMap<>() ;


        int count = clientRepo.updateClientsInBatch(clientsIds, clientMapper.toClient(clients).get(0)) ;

        serviceMessage.put("status", true) ;
        serviceMessage.put("message", "Number Of Updated Clients is :" + count) ;

        return serviceMessage ;
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




    /**
     * Files mangements methods 
     */

    @ValidateClients
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
        
        List<ClientDocs> clientDocs =  clientMapper.toClientDocs(files, metaData , client) ;
        clientDocs = clientRepo.insertClientDocs(clientDocs) ;

        return clientMapper.fromClientDocs(clientDocs) ;
    }


    public List<FileResponseDto> listFiles (long id) {

        try {
            Client client = clientRepo.find(id) ;

            return clientMapper.fromClientDocs(
                client.getClientDocs()
            );

        } catch (NoResultException e) {
            throw new UnableToProccessIteamException(ClientService.ERRORMESSAGE) ;
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
            throw new UnableToProccessIteamException(ClientService.ERRORMESSAGE) ;
        }

    }
}
