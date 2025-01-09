package ma.crm.carental.aspectj;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import ma.crm.carental.dtos.interfaces.ContractIdentifiable;
import ma.crm.carental.dtos.interfaces.DeliveryGuyIdentifiable;
import ma.crm.carental.exception.UnableToProccessIteamException;

@Slf4j
@Aspect
@Component
public class ContractChecker {
    
	private static final String ERRORMESSAGE = "access denied or unable to process the item , within the Contract" ;


    @PersistenceContext
    private EntityManager entityManager;

	@Before("@annotation(ma.crm.carental.annotations.ValidateContracts)")
	public void validateValidateContracts(JoinPoint joinPoint) throws UnableToProccessIteamException{

		Object[] args = joinPoint.getArgs();

		for (Object arg : args) {
			
			if (arg instanceof List<?>) {

				List<?> list = (List<?>) arg;

				if (!list.isEmpty() && list.get(0) instanceof ContractIdentifiable) {
					
					@SuppressWarnings("unchecked")
                	List<ContractIdentifiable> identifiableList = (List<ContractIdentifiable>) list;

					List<Long> contracts = identifiableList.stream()
							.map(ContractIdentifiable::getContract)
							.filter(Objects::nonNull)
							.distinct()  // Remove duplicates
							.collect(Collectors.toList());

					if (!contracts.isEmpty()) {
						validateContracts(contracts);
					}
					return;
				}
				
			}
		}
		
	}


    private void validateContracts(List<Long> contracts) throws UnableToProccessIteamException {

		Set<Long> uniqueContracts = new HashSet<>(contracts);

		String hql = "SELECT COUNT(c.id) FROM Contract c WHERE c.id IN :contractsIds";
		
		Query query = entityManager.createQuery(hql);
		query.setParameter("contractsIds", uniqueContracts);
		long number = (long) query.getSingleResult();
		
		entityManager.clear();
		
		if (number != uniqueContracts.size()) {
			throw new UnableToProccessIteamException(ContractChecker.ERRORMESSAGE);
		}
	}
}
