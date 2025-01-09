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
import ma.crm.carental.dtos.deliveryguy.DeliveryGuyRequestDto;
import ma.crm.carental.dtos.deliveryguy.DeliveryGuyResponseDto;
import ma.crm.carental.dtos.interfaces.validationgroups.CreateValidationGroup;
import ma.crm.carental.services.DeliveryGuyService;


@RestController
@RequestMapping("/riders")
@Validated
public class DeliveryGuyController {
    
    private final DeliveryGuyService deliveryGuyService ;

    @Autowired
    public DeliveryGuyController(
            DeliveryGuyService deliveryGuyService
        ) {
            this.deliveryGuyService = deliveryGuyService ;
        }

    
    @PostMapping
    @Validated(CreateValidationGroup.class)
    List<DeliveryGuyResponseDto> save(
        @RequestBody @Valid List<DeliveryGuyRequestDto> deliveryGuyRequestDtos
    ) {

        /**
         * @see validate and generate the ValidationException with errors .
         */
        return deliveryGuyService.saveDeliveryGuys(deliveryGuyRequestDtos) ;
    }

    @DeleteMapping("/{ids}")
    Map<String , Object> delete(
        @PathVariable List<Long> ids
    ){
        return deliveryGuyService.deleteDeliveryGuys(ids) ;
    }



    @PutMapping("/{ids}")
    @Validated
    Map<String , Object> update(
        @PathVariable List<Long> ids ,
        @RequestBody @Valid DeliveryGuyRequestDto deliveryGuyRequestDto 
    ){

        List<DeliveryGuyRequestDto> deliveryGuyRequestDtos = new ArrayList<>();
        deliveryGuyRequestDtos.add(deliveryGuyRequestDto);
        return deliveryGuyService.updateDeliveryGuys(ids, deliveryGuyRequestDtos) ;
    }

    @GetMapping
    Page<DeliveryGuyResponseDto> pagenate(
        @RequestParam int page ,
        @RequestParam int pageSize
    ) {

        return deliveryGuyService.pagenateDeliveryGuys(PageRequest.of(page, pageSize)) ;
    }

    @GetMapping("/{id}")
    DeliveryGuyResponseDto find(
        @PathVariable long id 
    ) {
        return deliveryGuyService.findDeliveryGuy(id) ;
    }

}
