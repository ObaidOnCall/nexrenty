package ma.crm.carental.repositories;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import ma.crm.carental.entities.DeliveryGuy;
import ma.crm.carental.repositories.interfaces.DeliveryGuyInterface;
import ma.crm.carental.utils.DBUtiles;


@Slf4j
@Repository
public class DeliveryGuyRepo implements DeliveryGuyInterface{

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private  int batchSize = 10;

    @PersistenceContext
    private EntityManager em ;

    @Override
    public List<DeliveryGuy> insertDeliveryGuyInBatch(List<DeliveryGuy> deliveryGuys) {


        if (deliveryGuys == null || deliveryGuys.isEmpty()) {
            return deliveryGuys ;
        }

        for (int i = 0; i < deliveryGuys.size(); i++) {
            
            em.persist(deliveryGuys.get(i));

            if (i > 0 && i % batchSize == 0) {
                em.flush();
                em.clear();
            }
        }
        
        return deliveryGuys ;
    }

    @Override
    public int deleteDeliveryGuys(List<Long> deliveryGuyIds) {

        if (deliveryGuyIds == null || deliveryGuyIds.isEmpty()) {
            return 0; // No records to delete
        }

        String jpql = "DELETE FROM DeliveryGuy d WHERE d.id IN :ids" ;

        return em.createQuery(jpql)
                    .setParameter("ids", deliveryGuyIds)
                    .executeUpdate() ;
    }

    @Override
    public int updateDeliveryGuysInBatch(List<Long> deliveryGuyIds, DeliveryGuy deliveryGuy) {

        int totalUpdatedRecords = 0;

        Map<String, Object> fieldsToUpdate = DBUtiles.convertToMap(deliveryGuy);

        //@ Check if there are any fields to update before entering the loop
        if (fieldsToUpdate.isEmpty()) {
            throw new IllegalArgumentException("No fields to update");
        }

        //@ Validate field names to prevent injection and errors
        Set<String> validFieldNames = Arrays.stream(DeliveryGuy.class.getDeclaredFields())
            .map(Field::getName)
            .collect(Collectors.toSet());

        //? Build the JPQL query dynamically based on non-null fields
        StringBuilder jpql = new StringBuilder("UPDATE DeliveryGuy d SET ");
        Map<String, Object> params = new HashMap<>();

        for (Map.Entry<String, Object> entry : fieldsToUpdate.entrySet()) {
            String fieldName = entry.getKey();
            Object value = entry.getValue();
            
            if (validFieldNames.contains(fieldName) && value != null) {
                jpql.append("d.").append(fieldName).append(" = :").append(fieldName).append(", ");
                params.put(fieldName, value);
            }
        }
        
        //? Remove the trailing comma from the query string
        jpql.setLength(jpql.length() - 2);  // Remove last comma
        jpql.append(" WHERE d.id IN :clientIds");


        //? Create the query with the base JPQL
        var query = em.createQuery(jpql.toString());
    
        //$ Set parameters for non-null fields
        for (Map.Entry<String, Object> param : params.entrySet()) {
            query.setParameter(param.getKey(), param.getValue());
        }
        
        
        
        /**
         * $ @apiNote plz update { ClientRepo.updateClientsInBatch} 
         * 
         * */

        for (int i = 0; i < deliveryGuyIds.size(); i += batchSize) {
            List<Long> batch = deliveryGuyIds.subList(i, Math.min(i + batchSize, deliveryGuyIds.size()));
    
            
            // Set the client IDs for the current batch
            query.setParameter("clientIds", batch);

            // Execute the update
            int updatedRecords = query.executeUpdate();
            totalUpdatedRecords += updatedRecords;
    
            em.flush();
            em.clear();
        }
        

        return totalUpdatedRecords ;
    }

    @Override
    public List<DeliveryGuy> deliveryGuysWithPagination(int page, int pageSize) {
        String jpql = "SELECT d FROM DeliveryGuy d ORDER BY d.id";

        TypedQuery<DeliveryGuy> query = em.createQuery(jpql, DeliveryGuy.class);

        query.setFirstResult(page * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList() ;
    }

    @Override
    public DeliveryGuy find(long id) throws NoResultException{
        String hql = "FROM DeliveryGuy d WHERE d.id = :id";
        DeliveryGuy deliveryGuy ;
            
        deliveryGuy = (DeliveryGuy) em.createQuery(hql)
                                    .setParameter("id", id)
                                    .getSingleResult() ;
        return deliveryGuy ;
    }

    @Override
    public Long count() {
        
        String jpql = "SELECT COUNT(d) FROM DeliveryGuy d";

        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        
        return query.getSingleResult() ;
    }

    

    


    
}
