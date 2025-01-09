package ma.crm.carental.repositories;


import jakarta.persistence.Entity;
import java.lang.reflect.Field;

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
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import ma.crm.carental.entities.Contract;
import ma.crm.carental.repositories.interfaces.ContractInterface;
import ma.crm.carental.utils.DBUtiles;


@Slf4j
@Repository
public class ContractRepo implements ContractInterface{


    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private  int batchSize = 10;


    @PersistenceContext
    private EntityManager em ;


    @Override
    public List<Contract> insertContractInBatch(List<Contract> contracts) {

        if (contracts == null || contracts.isEmpty()) {
            return contracts ;
        }

        for (int i = 0; i < contracts.size(); i++) {
            
            em.persist(contracts.get(i));

            if (i > 0 && i % batchSize == 0) {
                em.flush();
                em.clear();
            }
        }

        return contracts ;
    }

    @Override
    public int deleteContracts(List<Long> contractIds) {

        if (contractIds == null || contractIds.isEmpty()) {
            return 0; // No records to delete
        }

        String jpql = "DELETE FROM Contract c WHERE c.id IN :ids" ;

        return em.createQuery(jpql)
                    .setParameter("ids", contractIds)
                    .executeUpdate() ;
    }

    @Override
    public int updateContractsInBatch(List<Long> contractIds, Contract contract) {

        int totalUpdatedRecords = 0;


        Query query = DBUtiles.buildJPQLQueryDynamicallyForUpdate(contract, em) ;
        
        
        
        /**
         * $ @apiNote plz update { ClientRepo.updateClientsInBatch} 
         * 
         * */
        for (int i = 0; i < contractIds.size(); i += batchSize) {
            List<Long> batch = contractIds.subList(i, Math.min(i + batchSize, contractIds.size()));
    
            
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
    public List<Contract> contractsWithPagination(int page, int pageSize) {

        String jpql = "SELECT c FROM Contract c ORDER BY c.id";

        TypedQuery<Contract> query = em.createQuery(jpql, Contract.class);

        query.setFirstResult(page * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList() ;
    }

    @Override
    public Contract find(long id) throws NoResultException{

        String hql = "FROM Contract c WHERE c.id = :id";
            
        return (Contract) em.createQuery(hql)
                                    .setParameter("id", id)
                                    .getSingleResult() ;
    }

    @Override
    public Long count() {

        String jpql = "SELECT COUNT(c) FROM Contract c";

        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        
        return query.getSingleResult() ;
    }
    

}
