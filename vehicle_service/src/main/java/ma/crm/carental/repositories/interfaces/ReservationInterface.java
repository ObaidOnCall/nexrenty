package ma.crm.carental.repositories.interfaces;

import java.util.List;

import ma.crm.carental.entities.Reservation;

public interface ReservationInterface {

    List<Reservation> insertReservationInBatch(List<Reservation> reservations) ;

    int deleteReservations(List<Long> reservationsIds ) ;

    int updateReservationsInBatch(List<Long> reservationIds , Reservation reservation) ;

    List<Reservation> reservationsWithPagination(int page, int pageSize) ;

    Reservation find(long id) ;

    Long count() ;
}