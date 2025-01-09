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
import ma.crm.carental.dtos.interfaces.validationgroups.CreateValidationGroup;
import ma.crm.carental.dtos.reservation.ReservationRequestDto;
import ma.crm.carental.dtos.reservation.ReservationResponseDto;
import ma.crm.carental.dtos.vehicule.VehResponseDto;
import ma.crm.carental.entities.Vehicule;
import ma.crm.carental.services.ReservationService;
import ma.crm.carental.services.VehiculeSerivce;
import ma.crm.carental.tenantfilter.TenantContext;

@RestController
@RequestMapping("/agency")
@Validated
public class RentalAgencyController {


    private final ReservationService reservationService ;

    private final VehiculeSerivce vehiculeSerivce ;



    @Autowired
    RentalAgencyController(
        ReservationService reservationService ,
        VehiculeSerivce vehiculeSerivce
    ) {
        this.reservationService = reservationService ;
        this.vehiculeSerivce = vehiculeSerivce ;
    }

    @PostMapping("/{agencyId}/reservations")
    @Validated(CreateValidationGroup.class)
    List<ReservationResponseDto> save(
        @PathVariable String agencyId ,
        @RequestBody @Valid List<ReservationRequestDto> reservationRequestDtos
    ) {

        TenantContext.setTenantId(agencyId);
        
        return reservationService.saveReservations(reservationRequestDtos) ;
    }
    


    @GetMapping("/{agencyId}/vehicles")
    Page<Vehicule> pagenate(
        @PathVariable String agencyId ,
        @RequestParam int page ,
        @RequestParam int pageSize
    ) {
        TenantContext.setTenantId(agencyId);
        return vehiculeSerivce.vehsPaginate(page, pageSize) ;
    }


    @GetMapping("/{agencyId}/vehicles/{id}")
    public VehResponseDto find(
        @PathVariable String agencyId ,
        @PathVariable Long id
    ) {
        TenantContext.setTenantId(agencyId);
        return vehiculeSerivce.showVeh(id) ;
    }
}
