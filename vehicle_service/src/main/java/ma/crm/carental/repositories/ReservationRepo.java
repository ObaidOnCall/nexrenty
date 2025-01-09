package ma.crm.carental.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import ma.crm.carental.entities.Charge;
import ma.crm.carental.entities.Reservation;
import ma.crm.carental.repositories.interfaces.ReservationInterface;
import ma.crm.carental.utils.DBUtiles;


@Repository
public class ReservationRepo implements ReservationInterface{


    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private  int batchSize = 10;


    @PersistenceContext
    private EntityManager em ;

    @Override
    public List<Reservation> insertReservationInBatch(List<Reservation> reservations) {
        
        if (reservations == null || reservations.isEmpty()) {
            return reservations ;
        }

        for (int i = 0; i < reservations.size(); i++) {
            
            em.persist(reservations.get(i));

            if (i > 0 && i % batchSize == 0) {
                em.flush();
                em.clear();
            }
        }

        return reservations ;
    }

    @Override
    public int deleteReservations(List<Long> reservationsIds) {

        if (reservationsIds == null || reservationsIds.isEmpty()) {
            return 0; // No records to delete
        }

        String jpql = "DELETE FROM Reservation r WHERE r.id IN :ids" ;

        return em.createQuery(jpql)
                    .setParameter("ids", reservationsIds)
                    .executeUpdate() ;
    }

    @Override
    public int updateReservationsInBatch(List<Long> reservationIds, Reservation reservation) {

        int totalUpdatedRecords = 0;


        Query query = DBUtiles.buildJPQLQueryDynamicallyForUpdate(reservation, em) ;
        
        
        for (int i = 0; i < reservationIds.size(); i += batchSize) {
            List<Long> batch = reservationIds.subList(i, Math.min(i + batchSize, reservationIds.size()));
    
            
            // Set the client IDs for the current batch
            query.setParameter("Ids", batch);

            // Execute the update
            int updatedRecords = query.executeUpdate();
            totalUpdatedRecords += updatedRecords;
    
            em.flush();
            em.clear();
        }
        

        return totalUpdatedRecords ;
    }

    @Override
    public List<Reservation> reservationsWithPagination(int page, int pageSize) {

        String jpql = "SELECT r FROM Reservation r ORDER BY r.id";

        TypedQuery<Reservation> query = em.createQuery(jpql, Reservation.class);

        query.setFirstResult(page * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList() ;
    }

    @Override
    public Reservation find(long id) throws NoResultException{
        String hql = "FROM Reservation r WHERE r.id = :id";
            
        return (Reservation) em.createQuery(hql)
                                    .setParameter("id", id)
                                    .getSingleResult() ;
    }

    @Override
    public Long count() {

        String jpql = "SELECT COUNT(r) FROM Reservation r";

        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        
        return query.getSingleResult() ;
    }
    
}
