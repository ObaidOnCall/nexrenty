package com.nexrenty.clients_service.web;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nexrenty.clients_service.dtos.ClientRequestDto;
import com.nexrenty.clients_service.dtos.ClientResponseDto;
import com.nexrenty.clients_service.dtos.DeleteUpdateResponseDto;
import com.nexrenty.clients_service.dtos.ErrorResponse;
import com.nexrenty.clients_service.dtos.interfaces.CreateValidationGroup;
import com.nexrenty.clients_service.dtos.interfaces.UpdateValidationGroup;
import com.nexrenty.clients_service.services.ClientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;


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
    @Operation(
        summary = "Save multiple clients",
        description = "Saves a list of clients . Validates the input data using the CreateValidationGroup."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Clients saved successfully",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ClientResponseDto.class))
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request. This could be due to invalid or missing data in the request body.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class) // Define the structure of the 400 response
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error. This could be due to a problem with the database or an unexpected error."
        )
    })
    @Validated(CreateValidationGroup.class)
    List<ClientResponseDto> save(

        @Parameter(
            description = "List of client data to be saved",
            required = true,
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = ClientRequestDto.class))
            )
        )
        @RequestBody @Valid List<ClientRequestDto> clientRequestDtos
    ) {


        return clientService.saveClients(clientRequestDtos) ;
    }

    @DeleteMapping("/{ids}")
    @Operation(
        summary = "Delete clients",
        description = "Deletes a list of clients by their IDs."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Clients deleted successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = DeleteUpdateResponseDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request. This could be due to invalid client IDs.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    public DeleteUpdateResponseDto delete(
        @Parameter(
            description = "Comma-separated list of client IDs to be deleted",
            required = true,
            example = "1,2,3",
            schema = @Schema(type = "string")
        )
        @PathVariable List<Long> ids
    ){
        return clientService.deleteClients(ids) ;
    }


    /**
     *{@link ma.crm.carental.web.ClientController.save}
     */
    @PutMapping("/{ids}")
    @Operation(
        summary = "Update clients",
        description = "Updates a list of clients by their IDs."
    )
    @Validated(UpdateValidationGroup.class)
    public DeleteUpdateResponseDto update(
        @Parameter(
            description = "Comma-separated list of client IDs to be updated",
            required = true,
            example = "1,2,3",
            schema = @Schema(type = "string")
        )
        @PathVariable List<Long> ids ,

        @Parameter(
            description = "Client data to be updated",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ClientRequestDto.class)
            )
        )
        @RequestBody @Valid ClientRequestDto clientRequestDto
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

}