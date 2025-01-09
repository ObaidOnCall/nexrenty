package ma.crm.carental.repositories;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import ma.crm.carental.entities.Client;
import ma.crm.carental.entities.ClientDocs;
import ma.crm.carental.repositories.interfaces.ClientRepoInterface;
import ma.crm.carental.tenantfilter.TenantContext;


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

        String jpql = "UPDATE Client c SET " +
                    "c.firstname = COALESCE(:firstname, c.firstname), " +
                    "c.lastname = COALESCE(:lastname, c.lastname), " +
                    "c.cinOrPassport = COALESCE(:cinOrPassport, c.cinOrPassport), " +
                    "c.licence = COALESCE(:licence, c.licence), " +
                    "c.nationality = COALESCE(:nationality, c.nationality), " +
                    "c.address = COALESCE(:address, c.address), " +
                    "c.ville = COALESCE(:ville, c.ville), " +
                    "c.codePostal = COALESCE(:codePostal, c.codePostal), " +
                    "c.phone1 = COALESCE(:phone1, c.phone1), " +
                    "c.phone2 = COALESCE(:phone2, c.phone2), " +
                    "c.email = COALESCE(:email, c.email), " +
                    "c.cinIsValideUntil = COALESCE(:cinIsValideUntil, c.cinIsValideUntil), " +
                    "c.licenceIsValideUntil = COALESCE(:licenceIsValideUntil, c.licenceIsValideUntil), " +
                    "c.clientType = COALESCE(:clientType, c.clientType) " +
                    "WHERE c.id IN :clientIds";
        
        for (int i = 0; i < clintesIds.size(); i+= batchSize) {
            
            List<Long> batch = clintesIds.subList(i, Math.min(i+batchSize, clintesIds.size()))  ;

            int updatedRecords = em.createQuery(jpql)
                    .setParameter("firstname", updatedClient.getFirstname())
                    .setParameter("lastname", updatedClient.getLastname())
                    .setParameter("cinOrPassport", updatedClient.getCinOrPassport())
                    .setParameter("licence", updatedClient.getLicence())
                    .setParameter("nationality", updatedClient.getNationality())
                    .setParameter("address", updatedClient.getAddress())
                    .setParameter("ville", updatedClient.getVille())
                    .setParameter("codePostal", updatedClient.getCodePostal())
                    .setParameter("phone1", updatedClient.getPhone1())
                    .setParameter("phone2", updatedClient.getPhone2())
                    .setParameter("email", updatedClient.getEmail())
                    .setParameter("cinIsValideUntil", updatedClient.getCinIsValideUntil())
                    .setParameter("licenceIsValideUntil", updatedClient.getLicenceIsValideUntil())
                    .setParameter("clientType", updatedClient.getClientType())
                    .setParameter("clientIds", batch)
                    .executeUpdate();

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
}
