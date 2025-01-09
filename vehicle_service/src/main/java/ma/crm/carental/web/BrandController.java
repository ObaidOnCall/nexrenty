package ma.crm.carental.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import ma.crm.carental.annotations.ReactiveValidation;
import ma.crm.carental.dtos.vehicule.BrandRequsetDto;
import ma.crm.carental.dtos.vehicule.BrandResponseDto;
import ma.crm.carental.dtos.vehicule.ModelRequestDto;
import ma.crm.carental.dtos.vehicule.ModelResponseDto;
import ma.crm.carental.dtos.vehicule.ModelUpdateRequestDto;
import ma.crm.carental.dtos.vehicule.VehRequsetDto;
import ma.crm.carental.exception.UnableToProccessIteamException;
import ma.crm.carental.exception.ValidationErrorResponse;
import ma.crm.carental.exception.ValidationException;
import ma.crm.carental.services.BrandService;


@RestController
@RequestMapping("/brands")
public class BrandController {
    
    private final BrandService brandService ;

    BrandController (BrandService brandService) {
        this.brandService = brandService ;
    }

    /**
     * @apiNote Brand methods
     * @param brandRequsetDtos
     * @return
     */

    @PostMapping
    @ReactiveValidation
    public List<BrandResponseDto> save(
            @Valid @RequestBody List<BrandRequsetDto> brandRequsetDtos,
            @AuthenticationPrincipal Jwt jwt ,
            BindingResult bindingResult
        ){
        
        return brandService.saveBrands(brandRequsetDtos , (String)jwt.getClaims().get("sub")) ;
    }

    @GetMapping()
    public List<BrandResponseDto> listBrands(
        
    ) {

        return brandService.listBrands() ;
    }


    @GetMapping("/{id}")
    public BrandResponseDto showBrand(
    @PathVariable Long id ) throws UnableToProccessIteamException{

        return brandService.showBrand(id) ;
    }






    /**
     * @apiNote Model methods
     * @param brandRequsetDtos
     * @return
     */


    @PostMapping("/models")
    @ReactiveValidation
    public List<ModelResponseDto> saveModel(
        @Valid @RequestBody List<ModelRequestDto> modelRequestDtos ,
        BindingResult bindingResult
    ) {
        /**
         * @see you check the brand sended if is it owend by the current client .
         */
        return brandService.saveModels(modelRequestDtos) ;
    }

    @GetMapping("/{id}/models")
    public List<ModelResponseDto> listModels(
    @PathVariable Long id ) throws UnableToProccessIteamException{

        return brandService.listModels(id) ;
    }

    
    @PutMapping("/models/{ids}")
    public ResponseEntity<Map<String , Object>> updateModels(
        @PathVariable Long[] ids ,
        @RequestBody ModelUpdateRequestDto modelRequestDto 
    ) {
        List<ModelUpdateRequestDto> modelRequestDtos = new ArrayList<>() ;
        modelRequestDtos.add(modelRequestDto) ;

        Map<String , Object> response = brandService.updateModels(modelRequestDtos, ids) ;

        return new ResponseEntity<>(response , HttpStatus.OK) ;
    }
}
