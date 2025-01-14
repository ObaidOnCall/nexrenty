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
import ma.crm.carental.annotations.ReactiveValidation;
import ma.crm.carental.dtos.vehicule.AssuranceRequestDto;
import ma.crm.carental.dtos.vehicule.AssuranceResponseDto;
import ma.crm.carental.dtos.vehicule.VehRequsetDto;
import ma.crm.carental.dtos.vehicule.VehResponseDto;
import ma.crm.carental.entities.Vehicule;
import ma.crm.carental.exception.UnableToProccessIteamException;
import ma.crm.carental.exception.ValidationErrorResponse;
import ma.crm.carental.exception.ValidationException;
import ma.crm.carental.services.VehiculeSerivce;


@RestController
@RequestMapping("/vehicles")
public class VehiculeController {
    
    private final VehiculeSerivce vehiculeSerivce ;

    public VehiculeController (VehiculeSerivce vehiculeSerivce) {
        this.vehiculeSerivce = vehiculeSerivce ;
    }


    @PostMapping
    @ReactiveValidation
    public List<VehResponseDto> saveVehicules(
            @Valid @RequestBody List<VehRequsetDto> vehrequestList ,
            BindingResult bindingResult
        ) {
            
        return vehiculeSerivce.saveVehs(vehrequestList ) ;
    }

    @GetMapping()
    public Page<Vehicule> paginate(@RequestParam int page , @RequestParam int size) {
        /**
         * @@ get the current authentication object form SecurityContextHolder .
         */
        return vehiculeSerivce.vehsPaginate(page, size) ;
    }

    @DeleteMapping("/{ids}")
    public ResponseEntity<Map<String , Object>> deleteVehicules(
        @PathVariable List<Long> ids
    ) throws UnableToProccessIteamException {
        Map<String , Object> response = vehiculeSerivce.deleteVehs(ids);

        return new ResponseEntity<>(response , HttpStatus.OK) ;
        
    }

    @PutMapping("/{ids}")
    public ResponseEntity<Map<String , Object>> updateVehicules(
                @PathVariable List<Long> ids ,
                @RequestBody VehRequsetDto vehRequsetDto
            ){
        
        
                List<VehRequsetDto> vehRequsetDtos = new ArrayList<>() ;
                vehRequsetDtos.add(vehRequsetDto) ;

                Map<String , Object> response = vehiculeSerivce.updateVehs(vehRequsetDtos, ids) ;
                return  new ResponseEntity<>(response , HttpStatus.OK);
                
    }

    @GetMapping("/{id}")
    public VehResponseDto showVehicule(
        @PathVariable Long id
    ) {

        return vehiculeSerivce.showVeh(id) ;
    }



    /**
     * @apiNote assurance conttorerlerfkkfkjfkjjdfjjf
     */

    @PostMapping("/insurances")
    public List<AssuranceResponseDto> saveAssurances(@Valid @RequestBody List<AssuranceRequestDto> assuranceRequestDtos) {
        return vehiculeSerivce.saveAssurances(assuranceRequestDtos) ;
    }






    @GetMapping("/test")
    public void test(){

        SecurityContext context = SecurityContextHolder.createEmptyContext() ;
        Authentication authentication = new TestingAuthenticationToken("username", "password" , "ROLE_USER") ;
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);

        /**
         * @see access to the security context holder
         */

        Authentication authentication_ = SecurityContextHolder.getContext().getAuthentication() ;
        Jwt jwt = (Jwt) authentication.getPrincipal() ;

    }
}
