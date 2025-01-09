package ma.crm.carental.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
import jakarta.validation.Valid;
import ma.crm.carental.annotations.ReactiveValidation;
import ma.crm.carental.dtos.client.ClientRequestDto;
import ma.crm.carental.dtos.client.ClientResponseDto;
import ma.crm.carental.dtos.docs.FileResponseDto;
import ma.crm.carental.dtos.docs.MetaData;
import ma.crm.carental.dtos.docs.PresignedURL;
import ma.crm.carental.dtos.interfaces.validationgroups.CreateValidationGroup;
import ma.crm.carental.dtos.interfaces.validationgroups.UpdateValidationGroup;
import ma.crm.carental.services.ClientService;


@RestController
@RequestMapping("/clients")
@Validated
public class ClientController {
    
    private final ClientService clientService ;

    @Autowired
    public ClientController(
        ClientService clientService 
    ){
        this.clientService = clientService ;
    }

    
    /**
     * {@link ma.crm.carental.annotations.ReactiveValidation} it validate request and generate 
     * {@link ma.crm.carental.exception.ValidationException} if there is any invaild data
    * @see validate and generate the ValidationException with errors .
    */
    @PostMapping
    @Validated(CreateValidationGroup.class)
    List<ClientResponseDto> save(
        @RequestBody @Valid List<ClientRequestDto> clientRequestDtos
    ) {

        /**
         * @see validate and generate the ValidationException with errors .
         */
        return clientService.saveClients(clientRequestDtos) ;
    }

    @DeleteMapping("/{ids}")
    Map<String , Object> delete(
        @PathVariable List<Long> ids
    ){
        return clientService.deleteClients(ids) ;
    }


    /**
     *{@link ma.crm.carental.web.ClientController.save}
     */
    @PutMapping("/{ids}")
    @Validated(UpdateValidationGroup.class)
    Map<String , Object> update(
        @PathVariable List<Long> ids ,
        @RequestBody @Valid ClientRequestDto clientRequestDto ,
        BindingResult bindingResult
    ){

        List<ClientRequestDto> clientRequestDtos = new ArrayList<>();
        clientRequestDtos.add(clientRequestDto);
        return clientService.updateClients(ids, clientRequestDtos) ;
    }

    @GetMapping
    Page<ClientResponseDto> pagenateClients(
        @RequestParam int page ,
        @RequestParam int pageSize
    ) {

        return clientService.pagenateClients(PageRequest.of(page, pageSize)) ;
    }

    @GetMapping("/{id}")
    ClientResponseDto findCient(
        @PathVariable long id 
    ) {
        return clientService.findClient(id) ;
    }


    @PostMapping(value = "/{id}/docs" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<FileResponseDto> uploadFiles(
        @PathVariable long id ,
        @RequestPart("files") List<MultipartFile> files
    ) throws MinioException, IOException, InvalidKeyException, NoSuchAlgorithmException, IllegalArgumentException {
        
        MetaData metaData = new MetaData();
        metaData.setClient(id) ;
        
        List<MetaData> listMetaDatas = new ArrayList<>() ;
        listMetaDatas.add(metaData) ;


        return clientService.upload(listMetaDatas, files);
    }

    @GetMapping("/{id}/docs")
    public List<FileResponseDto> listFiles (
        @PathVariable long id
    ){
        return clientService.listFiles(id) ;
    }


    @GetMapping("/docs/{id}")
    public PresignedURL presignedUrl (
        @PathVariable long id
    ) throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException, XmlParserException, ServerException, IllegalArgumentException, IOException{
        return clientService.presignedUrl(id) ;
    }

}
