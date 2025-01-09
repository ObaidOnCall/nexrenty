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
import ma.crm.carental.dtos.charge.ChargeRequestDto;
import ma.crm.carental.dtos.charge.ChargeResponseDto;
import ma.crm.carental.dtos.interfaces.validationgroups.CreateValidationGroup;
import ma.crm.carental.dtos.interfaces.validationgroups.UpdateValidationGroup;
import ma.crm.carental.services.ChargeService;



@RestController
@RequestMapping("/charges")
@Validated
public class ChargeController {

    private final  ChargeService chargeService ;


    @Autowired
    ChargeController(
        ChargeService chargeService
    ) {
        this.chargeService = chargeService ;
    }

    @PostMapping
    @Validated(CreateValidationGroup.class)
    List<ChargeResponseDto> save(
        @RequestBody @Valid List<ChargeRequestDto> chargeRequestDtos
    ) {

        /**
         * @see validate and generate the ValidationException with errors .
         */
        return chargeService.saveCharges(chargeRequestDtos) ;
    }
    
    @DeleteMapping("/{ids}")
    Map<String , Object> delete(
        @PathVariable List<Long> ids
    ){
        return chargeService.deleteCharges(ids) ;
    }

    @PutMapping("/{ids}")
    @Validated(UpdateValidationGroup.class)
    Map<String , Object> update(
        @PathVariable List<Long> ids ,
        @RequestBody @Valid ChargeRequestDto chargeRequestDto 
    ){

        List<ChargeRequestDto> chargeRequestDtos = new ArrayList<>();
        chargeRequestDtos.add(chargeRequestDto);
        return chargeService.updateCharges(ids, chargeRequestDtos) ;
    }

    @GetMapping
    Page<ChargeResponseDto> pagenate(
        @RequestParam int page ,
        @RequestParam int pageSize
    ) {

        return chargeService.pagenateCharges(PageRequest.of(page, pageSize)) ;
    }


    @GetMapping("/{id}")
    ChargeResponseDto find(
        @PathVariable long id 
    ) {
        return chargeService.findCharge(id) ;
    }
}
