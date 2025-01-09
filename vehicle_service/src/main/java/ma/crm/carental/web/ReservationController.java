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
import ma.crm.carental.dtos.interfaces.validationgroups.UpdateValidationGroup;
import ma.crm.carental.dtos.reservation.ReservationRequestDto;
import ma.crm.carental.dtos.reservation.ReservationResponseDto;
import ma.crm.carental.services.ReservationService;

@RestController
@RequestMapping("/reservations")
@Validated
public class ReservationController {
    
    private final ReservationService reservationService ;

    @Autowired
    ReservationController(
        ReservationService reservationService
    ){
        this.reservationService = reservationService ;
    }


    @PostMapping
    @Validated(CreateValidationGroup.class)
    List<ReservationResponseDto> save(
        @RequestBody @Valid List<ReservationRequestDto> reservationRequestDtos
    ) {

        return reservationService.saveReservations(reservationRequestDtos) ;
    }


    @DeleteMapping("/{ids}")
    Map<String , Object> delete(
        @PathVariable List<Long> ids
    ){
        return reservationService.deleteReservations(ids) ;
    }


    @PutMapping("/{ids}")
    @Validated(UpdateValidationGroup.class)
    Map<String , Object> update(
        @PathVariable List<Long> ids ,
        @RequestBody @Valid ReservationRequestDto reservationRequestDto 
    ){

        List<ReservationRequestDto> reservationRequestDtos = new ArrayList<>();
        reservationRequestDtos.add(reservationRequestDto);
        return reservationService.updateReservations(ids, reservationRequestDtos) ;
    }

    @GetMapping
    Page<ReservationResponseDto> pagenate(
        @RequestParam int page ,
        @RequestParam int pageSize
    ) {

        return reservationService.pagenateReservations(PageRequest.of(page, pageSize)) ;
    }

    @GetMapping("/{id}")
    ReservationResponseDto find(
        @PathVariable long id 
    ) {
        return reservationService.findReservation(id) ;
    }
}
