package com.nexrenty.clients_service.repositories.interfaces;

import java.util.List;

import com.nexrenty.clients_service.entities.Client;
import com.nexrenty.clients_service.entities.ClientDocs;



public interface ClientRepoInterface {

    List<Client> insertClientInBatch(List<Client> clients) ;

    int deleteClients(List<Long> clintesIds ) ;

    int updateClientsInBatch(List<Long> clintesIds , Client client) ;

    List<Client> clientsWithPagination(int page, int pageSize) ;

    Client find(long id) ;

    List<Client> findClients(List<Long> ids) ;

    Long count() ;

    List<ClientDocs> insertClientDocs(List<ClientDocs> clientDocs) ;

    ClientDocs findClientDocs(long id) ;
    
}