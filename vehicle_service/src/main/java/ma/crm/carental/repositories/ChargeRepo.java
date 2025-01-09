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
import ma.crm.carental.repositories.interfaces.ChargeInterface;
import ma.crm.carental.utils.DBUtiles;


@Repository
public class ChargeRepo implements ChargeInterface{


    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private  int batchSize = 10;


    @PersistenceContext
    private EntityManager em ;

    @Override
    public List<Charge> insertChargeInBatch(List<Charge> charges) {

        if (charges == null || charges.isEmpty()) {
            return charges ;
        }

        for (int i = 0; i < charges.size(); i++) {
            
            em.persist(charges.get(i));

            if (i > 0 && i % batchSize == 0) {
                em.flush();
                em.clear();
            }
        }

        return charges ;
    }

    @Override
    public int deleteCharges(List<Long> chargesIds) {

        if (chargesIds == null || chargesIds.isEmpty()) {
            return 0; // No records to delete
        }

        String jpql = "DELETE FROM Charge c WHERE c.id IN :ids" ;

        return em.createQuery(jpql)
                    .setParameter("ids", chargesIds)
                    .executeUpdate() ;
    }

    @Override
    public int updateChargesInBatch(List<Long> chargeIds, Charge charge) {

        int totalUpdatedRecords = 0;


        Query query = DBUtiles.buildJPQLQueryDynamicallyForUpdate(charge, em) ;
        
        
        for (int i = 0; i < chargeIds.size(); i += batchSize) {
            List<Long> batch = chargeIds.subList(i, Math.min(i + batchSize, chargeIds.size()));
    
            
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
    public List<Charge> chargesWithPagination(int page, int pageSize) {

        String jpql = "SELECT c FROM Charge c ORDER BY c.id";

        TypedQuery<Charge> query = em.createQuery(jpql, Charge.class);

        query.setFirstResult(page * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList() ;
    }

    @Override
    public Charge find(long id) throws NoResultException{

        String hql = "FROM Charge c WHERE c.id = :id";
            
        return (Charge) em.createQuery(hql)
                                    .setParameter("id", id)
                                    .getSingleResult() ;
    }

    @Override
    public Long count() {

        String jpql = "SELECT COUNT(c) FROM Charge c";

        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        
        return query.getSingleResult() ;
    }
    
}
