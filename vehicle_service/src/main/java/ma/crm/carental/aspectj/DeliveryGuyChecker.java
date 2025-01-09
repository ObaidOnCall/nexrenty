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
import ma.crm.carental.dtos.interfaces.ClientIdentifiable;
import ma.crm.carental.dtos.interfaces.DeliveryGuyIdentifiable;
import ma.crm.carental.exception.UnableToProccessIteamException;

@Slf4j
@Aspect
@Component
public class DeliveryGuyChecker {
    
	private static final String ERRORMESSAGE = "access denied or unable to process the item , within the rider guy" ;


    @PersistenceContext
    private EntityManager entityManager;

	@Before("@annotation(ma.crm.carental.annotations.ValidateDeliveryGuys)")
	public void validateDeliveryGuys(JoinPoint joinPoint) throws UnableToProccessIteamException{
		Object[] args = joinPoint.getArgs();

		for (Object arg : args) {
			
			if (arg instanceof List<?>) {

				List<?> list = (List<?>) arg;

				if (!list.isEmpty() && list.get(0) instanceof DeliveryGuyIdentifiable) {
					
					@SuppressWarnings("unchecked")
                	List<DeliveryGuyIdentifiable> identifiableList = (List<DeliveryGuyIdentifiable>) list;

					List<Long> deliveryGuys = identifiableList.stream()
							.map(DeliveryGuyIdentifiable::getDeliveryGuy)
							.filter(Objects::nonNull)
							.distinct()  // Remove duplicates
							.collect(Collectors.toList());

					if (!deliveryGuys.isEmpty()) {
						validateDeliveryGuys(deliveryGuys);
					}
					return;
				}
				
			}
		}
		
	}


    private void validateDeliveryGuys(List<Long> deliveryGuys) throws UnableToProccessIteamException {

		Set<Long> uniqueDeliveryGuys = new HashSet<>(deliveryGuys);

		String hql = "SELECT COUNT(d.id) FROM DeliveryGuy d WHERE d.id IN :deliveryGuysIds";
		
		Query query = entityManager.createQuery(hql);
		query.setParameter("deliveryGuysIds", uniqueDeliveryGuys);
		long number = (long) query.getSingleResult();
		
		entityManager.clear();
		
		if (number != uniqueDeliveryGuys.size()) {
			throw new UnableToProccessIteamException(DeliveryGuyChecker.ERRORMESSAGE);
		}
	}
}
