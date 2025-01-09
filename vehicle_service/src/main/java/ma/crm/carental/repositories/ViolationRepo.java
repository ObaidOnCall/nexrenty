package ma.crm.carental.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import ma.crm.carental.entities.Client;
import ma.crm.carental.entities.Violation;
import ma.crm.carental.repositories.interfaces.ViolationRepoInterface;



@Slf4j
@Repository
public class ViolationRepo implements ViolationRepoInterface{

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private  int batchSize = 10;

    @PersistenceContext
    private EntityManager em ;

    @Override
    public List<Violation> insertViolationInBatch(List<Violation> violations) {
        log.debug("batch size is : {} 🔖\n" , batchSize);

        if (violations == null || violations.isEmpty()) {
            return violations ;
        }

        for (int i = 0; i < violations.size(); i++) {
            
            em.persist(violations.get(i));

            if (i > 0 && i % batchSize == 0) {
                em.flush();
                em.clear();
            }
        }
        
        return violations ;
    }

    @Override
    public int deleteViolations(List<Long> violationsIds) {

        if (violationsIds == null || violationsIds.isEmpty()) {
            return 0; // No records to delete
        }

        String jpql = "DELETE FROM Violation v WHERE v.id IN :ids" ;

        return em.createQuery(jpql)
                    .setParameter("ids", violationsIds)
                    .executeUpdate() ;
    }

    @Override
    public int updateViolationsInBatch(List<Long> violationsIds, Violation violation) {
        
        int totalUpdatedRecords = 0;

        String jpql = "UPDATE Violation v SET " +
                    "v.description = COALESCE(:description, v.description), " +
                    "v.finAmount = COALESCE(:finAmount, v.finAmount), " +
                    "v.date = COALESCE(:date, v.date), " +
                    "v.isPaid = COALESCE(:isPaid, v.isPaid), " +
                    "v.client.id = COALESCE(:clientId, v.client.id), " +
                    "v.charge.id = COALESCE(:chargeId, v.charge.id) " +
                    "WHERE v.id IN :violationsIds";

        for (int i = 0; i < violationsIds.size(); i += batchSize) {

            List<Long> batch = violationsIds.subList(i, Math.min(i + batchSize, violationsIds.size()));

            int updatedRecords = em.createQuery(jpql)
                    .setParameter("description", violation.getDescription())
                    .setParameter("finAmount", violation.getFinAmount())
                    .setParameter("date", violation.getDate())
                    .setParameter("isPaid", violation.getIsPaid())
                    .setParameter("clientId", violation.getClientId() != null ? violation.getClientId() : null)
                    .setParameter("chargeId", violation.getCharge() != null ? violation.getCharge().getId() : null)
                    .setParameter("violationsIds", batch)
                    .executeUpdate();

            totalUpdatedRecords += updatedRecords;

            em.flush();
            em.clear();
        }

        return totalUpdatedRecords;

    }

    @Override
    public List<Violation> volationsWithPagination(int page, int pageSize) {
        
        String jpql = "SELECT c FROM Violation c ORDER BY c.id";

        TypedQuery<Violation> query = em.createQuery(jpql, Violation.class);

        query.setFirstResult(page * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList() ;

    }

    @Override
    public Violation find(long id) throws NoResultException{

        String hql = "FROM Violation c WHERE c.id = :id";
        Violation violation ;
            
        violation = (Violation) em.createQuery(hql)
                                .setParameter("id", id)
                                .getSingleResult() ;
        return violation ;
    }

    @Override
    public Long count() {

        String jpql = "SELECT COUNT(v) FROM Violation v";

        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        
        return query.getSingleResult() ;
    }
    
}
