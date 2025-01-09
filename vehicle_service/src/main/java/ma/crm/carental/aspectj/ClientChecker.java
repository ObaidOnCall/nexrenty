package ma.crm.carental.aspectj;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import ma.crm.carental.dtos.client.ClientResponseDto;
import ma.crm.carental.dtos.interfaces.ClientIdentifiable;
import ma.crm.carental.exception.UnableToProccessIteamException;
import ma.crm.carental.services.ClientServiceClient;

@Slf4j
@Aspect
@Component
public class ClientChecker {
    
	private static final String ERRORMESSAGE = "access denied or unable to process the item , within client" ;

	private final ClientServiceClient clientServiceClient ;

	@Autowired
	ClientChecker(
		ClientServiceClient clientServiceClient
	) {
		this.clientServiceClient = clientServiceClient ;
	}


    @PersistenceContext
    private EntityManager entityManager;

	@Before("@annotation(ma.crm.carental.annotations.ValidateClients)")
	public void validateClients(JoinPoint joinPoint) throws UnableToProccessIteamException{
		Object[] args = joinPoint.getArgs();

		for (Object arg : args) {
			
			if (arg instanceof List) {

				List<?> list = (List<?>) arg;

				if (!list.isEmpty() && list.get(0) instanceof ClientIdentifiable) {
					
					@SuppressWarnings("unchecked")
                	List<ClientIdentifiable> identifiableList = (List<ClientIdentifiable>) list;

					List<Long> clients = identifiableList.stream()
							.map(ClientIdentifiable::getClient)
							.filter(Objects::nonNull)
							.distinct()
							.toList();

					if (!clients.isEmpty()) {
						// validateClients(clients);
						validateClientsV2(clients) ;
					}
					return;
				}
				
			}
		}
		
	}


    private void validateClients(List<Long> clients) throws UnableToProccessIteamException {

		Set<Long> uniqueClients = new HashSet<>(clients);

		String hql = "SELECT COUNT(c.id) FROM Client c WHERE c.id IN :clientsIds";
		
		Query query = entityManager.createQuery(hql);
		query.setParameter("clientsIds", uniqueClients);
		long number = (long) query.getSingleResult();
		
		entityManager.clear();
		
		if (number != uniqueClients.size()) {
			throw new UnableToProccessIteamException(ClientChecker.ERRORMESSAGE);
		}
	}


	private void validateClientsV2(List<Long> clients) throws UnableToProccessIteamException {

		Set<Long> uniqueClients = new HashSet<>(clients);

		List<ClientResponseDto> clientResponseDtos = clientServiceClient.findClients(clients) ;

		log.debug("Clients retrieved: {} ✅", clientResponseDtos);
		
		if (clientResponseDtos.size() != uniqueClients.size()) {
			throw new UnableToProccessIteamException(ClientChecker.ERRORMESSAGE);
		}
	}
}
