package ma.crm.carental.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

import jakarta.validation.Valid;
import ma.crm.carental.dtos.contract.ContractRequestDto;
import ma.crm.carental.dtos.contract.ContractResponseDto;
import ma.crm.carental.dtos.deliveryguy.DeliveryGuyRequestDto;
import ma.crm.carental.dtos.deliveryguy.DeliveryGuyResponseDto;
import ma.crm.carental.dtos.interfaces.validationgroups.CreateValidationGroup;
import ma.crm.carental.dtos.interfaces.validationgroups.UpdateValidationGroup;
import ma.crm.carental.services.ContractService;

@RestController
@RequestMapping("/contracts")
@Validated
public class ContractController {

    private final ContractService contractService ;


    @Autowired
    ContractController(
        ContractService contractService
    ) {
        this.contractService = contractService ;
    }

    @PostMapping
    @Validated(CreateValidationGroup.class)
    List<ContractResponseDto> save(
        @RequestBody @Valid List<ContractRequestDto> contractRequestDtos
    ) {

        /**
         * @see validate and generate the ValidationException with errors .
         */
        return contractService.saveContracts(contractRequestDtos) ;
    }
    
    @DeleteMapping("/{ids}")
    Map<String , Object> delete(
        @PathVariable List<Long> ids
    ){
        return contractService.deleteContracts(ids) ;
    }

    
    @PutMapping("/{ids}")
    @Validated(UpdateValidationGroup.class)
    Map<String , Object> update(
        @PathVariable List<Long> ids ,
        @RequestBody @Valid ContractRequestDto contractRequestDto 
    ){

        List<ContractRequestDto> contractRequestDtos = new ArrayList<>();
        contractRequestDtos.add(contractRequestDto);
        return contractService.updateContracts(ids, contractRequestDtos) ;
    }

    @GetMapping
    Page<ContractResponseDto> pagenate(
        @RequestParam int page ,
        @RequestParam int pageSize
    ) {

        return contractService.pagenateContracts(PageRequest.of(page, pageSize)) ;
    }


    @GetMapping("/{id}")
    ContractResponseDto find(
        @PathVariable long id 
    ) {
        return contractService.findContract(id) ;
    }
}
