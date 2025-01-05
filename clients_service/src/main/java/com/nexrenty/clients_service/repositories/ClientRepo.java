package com.nexrenty.clients_service.repositories;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.nexrenty.clients_service.entities.Client;
import com.nexrenty.clients_service.entities.ClientDocs;
import com.nexrenty.clients_service.repositories.interfaces.ClientRepoInterface;
import com.nexrenty.clients_service.utils.DBUtiles;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;



@Slf4j
@Repository
public class ClientRepo implements ClientRepoInterface{

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private  int batchSize = 10;

    @PersistenceContext
    private EntityManager em ;

    @Override
    public List<Client> insertClientInBatch(List<Client> clients) {

        log.debug("batch size is : {} 🔖\n" , batchSize);

        if (clients == null || clients.isEmpty()) {
            return clients ;
        }

        for (int i = 0; i < clients.size(); i++) {
            
            em.persist(clients.get(i));

            if (i > 0 && i % batchSize == 0) {
                em.flush();
                em.clear();
            }
        }
        
        return clients ;
    }
    
    @Override
    public int deleteClients(List<Long> clintesIds) {

        if (clintesIds == null || clintesIds.isEmpty()) {
            return 0; // No records to delete
        }

        String jpql = "DELETE FROM Client c WHERE c.id IN :ids" ;

        return em.createQuery(jpql)
                    .setParameter("ids", clintesIds)
                    .executeUpdate() ;
    }

    @Override
    public int updateClientsInBatch(List<Long> clintesIds  , Client updatedClient) {

        int totalUpdatedRecords = 0 ;

        Query query = DBUtiles.buildJPQLQueryDynamicallyForUpdate(updatedClient, em) ;
        
        for (int i = 0; i < clintesIds.size(); i += batchSize) {
            List<Long> batch = clintesIds.subList(i, Math.min(i + batchSize, clintesIds.size()));
    
            
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
    public List<Client> clientsWithPagination(int page, int pageSize) {
        
        String jpql = "SELECT c FROM Client c ORDER BY c.id";

        TypedQuery<Client> query = em.createQuery(jpql, Client.class);

        query.setFirstResult(page * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList() ;

    }

    @Override
    public Client find(long id) throws NoResultException{

        String hql = "FROM Client c WHERE c.id = :id";
        Client client ;
            
        client = (Client) em.createQuery(hql)
                                .setParameter("id", id)
                                .getSingleResult() ;
        return client ;
    }


    @Override
    public Long count() {

        String jpql = "SELECT COUNT(c) FROM Client c";

        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        
        return query.getSingleResult() ;
    }

    @Override
    public List<ClientDocs> insertClientDocs(List<ClientDocs> clientDocs) {
        
        log.debug("batch size is : {} 🔖\n" , batchSize);

        if (clientDocs == null || clientDocs.isEmpty()) {
            return clientDocs ;
        }

        for (int i = 0; i < clientDocs.size(); i++) {
            
            em.persist(clientDocs.get(i));

            if (i > 0 && i % batchSize == 0) {
                em.flush();
                em.clear();
            }
        }
        
        return clientDocs ;
    }


    @Override
    public ClientDocs findClientDocs(long id) throws NoResultException{

        String hql = "FROM ClientDocs c WHERE c.id = :id";
        ClientDocs clientDocs ;
            
        clientDocs = (ClientDocs) em.createQuery(hql)
                                .setParameter("id", id)
                                .getSingleResult() ;
        return clientDocs ;
    }

    @Override
    public List<Client> findClients(List<Long> ids) {
        
        if (ids == null || ids.isEmpty()) {
            
            return Collections.emptyList();
        }
    
        String jpql = "SELECT c FROM Client c WHERE c.id IN :ids";
        
        return em.createQuery(jpql, Client.class)
                            .setParameter("ids", ids)
                            .getResultList();
    }

}
