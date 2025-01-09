package ma.crm.carental.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import ma.crm.carental.annotations.ValidateContracts;
import ma.crm.carental.annotations.ValidateVehicules;
import ma.crm.carental.dtos.charge.ChargeRequestDto;
import ma.crm.carental.dtos.charge.ChargeResponseDto;
import ma.crm.carental.dtos.reservation.ReservationRequestDto;
import ma.crm.carental.dtos.reservation.ReservationResponseDto;
import ma.crm.carental.entities.Charge;
import ma.crm.carental.entities.Reservation;
import ma.crm.carental.exception.UnableToProccessIteamException;
import ma.crm.carental.mappers.ReservationMapper;
import ma.crm.carental.repositories.ReservationRepo;

@Service
@Transactional
public class ReservationService {
    
    private static final String ERRORMESSAGE = "Access denied or unable to process the item within the ReservationServices. ReservationService ID: ";

    private final ReservationMapper reservationMapper ;
    private final ReservationRepo reservationRepo ;


    ReservationService(
        ReservationMapper reservationMapper ,
        ReservationRepo reservationRepo
    ) {
        this.reservationRepo = reservationRepo ;
        this.reservationMapper = reservationMapper ;
    }

    @ValidateVehicules
    public List<ReservationResponseDto> saveReservations(List<ReservationRequestDto> reservationRequestDtos){

        return reservationMapper.fromReservation(
                                reservationRepo.insertReservationInBatch(
                                            reservationMapper.toReservation(reservationRequestDtos)
                ));
    }

    public Map<String , Object> deleteReservations(List<Long> reservationsIds) {

        Map<String , Object> serviceMessage = new HashMap<>() ;
        
        int count = reservationRepo.deleteReservations(reservationsIds) ;

        serviceMessage.put("status", true) ;
        serviceMessage.put("message", String.format("Number Of Deleted Reservations is %d", count)) ;
        return serviceMessage ;
        
    }

    @ValidateVehicules
    public Map<String , Object> updateReservations(List<Long> reservationsIds , List<ReservationRequestDto> reservationRequestDtos) {

        Map<String , Object> serviceMessage = new HashMap<>() ;

        int count = reservationRepo.updateReservationsInBatch(reservationsIds, reservationMapper.toReservation(reservationRequestDtos).get(0)) ;

        serviceMessage.put("status", true) ;
        serviceMessage.put("message", "Number Of Updated Reservations is " + count) ;

        return serviceMessage ;
    }


    public Page<ReservationResponseDto> pagenateReservations(Pageable pageable) {
        
        Long totalElements = reservationRepo.count() ;


        List<ReservationResponseDto> reservationResponseDtos = reservationMapper.fromReservation(
            reservationRepo.reservationsWithPagination(pageable.getPageNumber(), pageable.getPageSize())
        ) ;

        return new PageImpl<>(reservationResponseDtos , pageable , totalElements) ;
    }


    public ReservationResponseDto findReservation(long id) {

        try {
            Reservation reservation = reservationRepo.find(id) ;

            List<Reservation> reservations = List.of(reservation) ;

            return reservationMapper.fromReservation(reservations).get(0) ;

        } catch (NoResultException e) {
            throw new UnableToProccessIteamException(ERRORMESSAGE + id) ;
        }
    }


}
