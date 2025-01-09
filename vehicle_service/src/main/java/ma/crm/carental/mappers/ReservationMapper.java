package ma.crm.carental.mappers;

import java.util.List;
import java.util.stream.Collectors;


import org.springframework.stereotype.Component;


import ma.crm.carental.dtos.reservation.ReservationRequestDto;
import ma.crm.carental.dtos.reservation.ReservationResponseDto;
import ma.crm.carental.dtos.vehicule.VehResponseDto;
import ma.crm.carental.entities.Reservation;
import ma.crm.carental.entities.Vehicule;


@Component
public class ReservationMapper {


    public List<Reservation> toReservation(List<ReservationRequestDto> reservationRequestDtos) {

        return reservationRequestDtos.stream()
                                .map(reservationRequestDto ->
                                           Reservation.builder()
                                            .vehicule(
                                                Vehicule.builder()
                                                .id(reservationRequestDto.getVehicule())
                                                .build() 
                                            )
                                            .startDate(reservationRequestDto.getStartDate())
                                            .endDate(reservationRequestDto.getEndDate())
                                            .totalPrice(reservationRequestDto.getTotalPrice())
                                            .notes(reservationRequestDto.getNotes())
                                           .build()

                ).collect(Collectors.toList()) ;

    }
    

    public List<ReservationResponseDto> fromReservation(List<Reservation> reservations) {

        return reservations.stream().map(
            reservation ->  ReservationResponseDto.builder()
                            .id(reservation.getId())
                            .vehicule(
                                VehResponseDto.builder()
                                .id(reservation.getVehicule().getId())
                                .color(reservation.getVehicule().getColor())
                                .mileage(reservation.getVehicule().getMileage())
                                .price(reservation.getVehicule().getPrice())
                                .build()
                            )
                            .startDate(reservation.getStartDate())
                            .endDate(reservation.getEndDate())
                            .notes(reservation.getNotes())
                            .paymentStatus(reservation.isPaid())
                            .build()
        ).collect(Collectors.toList()) ;
    }
}
