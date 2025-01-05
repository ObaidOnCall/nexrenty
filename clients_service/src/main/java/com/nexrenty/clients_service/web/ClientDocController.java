package com.nexrenty.clients_service.web;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.http.MediaType;

import com.nexrenty.clients_service.dtos.ErrorResponse;
import com.nexrenty.clients_service.dtos.FileResponseDto;
import com.nexrenty.clients_service.dtos.MetaData;
import com.nexrenty.clients_service.dtos.PresignedURL;
import com.nexrenty.clients_service.services.ClientDocService;

import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.MinioException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/clients")
@Validated
@Tag(name = "Client Documents", description = "APIs for managing client documents")
public class ClientDocController {
    

    private final ClientDocService clientDocService ;

    @Autowired
    ClientDocController(
        ClientDocService clientDocService
    ) {
        this.clientDocService = clientDocService ;
    }


    @PostMapping(value = "/{id}/files" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
        summary = "Upload files for a client",
        description = "Uploads one or more files for a specific client identified by their ID. The files are associated with the client and stored in the system."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Files uploaded successfully"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request. This could be due to invalid file format, missing files, or invalid client ID." ,
            content = @Content(
                mediaType = "application/json" ,
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access forbidden. The client with the specified ID does not exist, or the requesting user does not have permission to upload files for this client.",
            content = @Content(
                mediaType = "application/json" ,
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error. This could be due to a problem with the file storage service or an unexpected error."
        )
        
    })
    public List<FileResponseDto> uploadFiles(
        @Parameter(
            description = "The ID of the client for whom the files are being uploaded",
            example = "188995",
            required = true
        )
        @PathVariable long id ,

        @Parameter(
            description = "List of files to upload. Each file should be provided as a multipart form data attachment.",
            required = true
        )
        @RequestPart("files") List<MultipartFile> files
        
    ) throws MinioException, IOException, InvalidKeyException, NoSuchAlgorithmException, IllegalArgumentException {
        
        MetaData metaData = new MetaData();
        metaData.setClient(id) ;
        
        List<MetaData> listMetaDatas = new ArrayList<>() ;
        listMetaDatas.add(metaData) ;


        return clientDocService.upload(listMetaDatas, files);
    }



    @GetMapping("/{id}/files")
    @Operation(summary = "List files for a client", description = "Retrieve a list of files associated with a client by their ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of files"),
        @ApiResponse(
            responseCode = "403",
            description = "Access forbidden. The client with the specified ID does not exist, or the requesting user does not have permission to access the files for this client." ,
            content = @Content(
                mediaType = "application/json" ,
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<FileResponseDto> listFiles (
        @Parameter(description = "The ID of the client", example = "188995", required = true)
        @PathVariable long id
    ){
        return clientDocService.listFiles(id) ;
    }


    @GetMapping("/files/{id}")
    @Operation(summary = "Get a presigned URL for a client document", description = "Retrieve a presigned URL to access a client document by its ID.")
    public PresignedURL presignedUrl (
        @Parameter(
            description = "The ID of the document",
            example = "123",
            required = true
        )
        @PathVariable long id
    ) throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException, XmlParserException, ServerException, IllegalArgumentException, IOException{
        return clientDocService.presignedUrl(id) ;
    }
}
