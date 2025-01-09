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
import ma.crm.carental.dtos.interfaces.VehiclueIdentifiable;
import ma.crm.carental.dtos.vehicule.AssuranceRequestDto;
import ma.crm.carental.dtos.vehicule.ModelRequestDto;
import ma.crm.carental.dtos.vehicule.ModelDtoInterface;
import ma.crm.carental.dtos.vehicule.VehRequsetDto;
import ma.crm.carental.exception.UnableToProccessIteamException;
import ma.crm.carental.tenantfilter.TenantContext;


@Slf4j
@Aspect
@Component
public class VehiculeChecker {


	private static final String ERRORMESSAGE = "access denied or unable to process the item ,please use a valid data" ;
	private static final String ERRORMESSAGEVEHICLUE = "access denied or unable to process the item within the vehicule" ;

	@PersistenceContext
    private EntityManager entityManager;

	@Before("@annotation(ma.crm.carental.annotations.ValidateVehiculeModels)")
	public void validateVehicleModels(JoinPoint joinPoint) throws UnableToProccessIteamException{
		Object[] args = joinPoint.getArgs();

    	// Assuming the first argument is always your DTO
		if (args.length > 0 && args[0] instanceof List) {
				@SuppressWarnings("unchecked")
				List<VehRequsetDto> vehrequests = (List<VehRequsetDto>) args[0];

				List<Long> models = vehrequests.stream()
						.map(VehRequsetDto::getModel)
						.filter(Objects::nonNull) // Filter out null models
						.collect(Collectors.toList());

				// Only validate if there are models to check
				if (!models.isEmpty()) {
					validateModels(models);
				}
		}

		
	}


	@Before("@annotation(ma.crm.carental.annotations.ValidateVehiculeBrands)")
	public void validateVehicleBrands(JoinPoint joinPoint) throws UnableToProccessIteamException{
		Object[] args = joinPoint.getArgs();

    	// Assuming the first argument is always your DTO
		if (args.length > 0 && args[0] instanceof List) {
				@SuppressWarnings("unchecked")
				List<ModelDtoInterface> modelRequestDtos = (List<ModelDtoInterface>) args[0];

				List<Long> brands = modelRequestDtos.stream()
						.map(ModelDtoInterface::getBrand)
						.filter(Objects::nonNull) // Filter out null models
						.collect(Collectors.toList());

				// Only validate if there are models to check
				if (!brands.isEmpty()) {
					validateBrands(brands);
				}
		}

		
	}



	@Before("execution(* ma.crm.carental.services.VehiculeSerivce.saveAssurances(..))")
	public void beforeAddAssurances(JoinPoint joinPoint) throws UnableToProccessIteamException{
		//Advice
		Object[] args = joinPoint.getArgs();
		@SuppressWarnings("unchecked")
		List<AssuranceRequestDto> assuranceRequestDtos = (List<AssuranceRequestDto>) args[0];

		List <Long> vehiList = assuranceRequestDtos.stream()
											.map(assurance -> assurance.getVehicle())
											.collect(Collectors.toList()) ;

		String hql = "SELECT COUNT(v.id) " +
		"FROM Vehicule v " +
		"WHERE v.tenantId = :tenantId " +
		"AND v.id IN :vehiclesIds";

		Query query = entityManager.createQuery(hql) ;
		query.setParameter("tenantId", TenantContext.getTenantId()) ;
		query.setParameter("vehiclesIds", vehiList) ;
		long number =(long) query.getSingleResult() ;
		
		entityManager.clear();
		if (number != vehiList.size()) {
			// All models are valid, proceed with vehicle creation
			throw new UnableToProccessIteamException(VehiculeChecker.ERRORMESSAGE) ;
		}
		
	}


	@Before("@annotation(ma.crm.carental.annotations.ValidateVehicules)")
	public void validateVehicles(JoinPoint joinPoint) throws UnableToProccessIteamException{
		Object[] args = joinPoint.getArgs();

		for (Object arg : args) {
			
			if (arg instanceof List<?>) {

				List<?> list = (List<?>) arg;

				if (!list.isEmpty() && list.get(0) instanceof VehiclueIdentifiable) {
					
					@SuppressWarnings("unchecked")
                List<VehiclueIdentifiable> identifiableList = (List<VehiclueIdentifiable>) list;

					List<Long> clients = identifiableList.stream()
							.map(VehiclueIdentifiable::getVehicule)
							.filter(Objects::nonNull)
							.distinct()  // Remove duplicates
							.collect(Collectors.toList());

					if (!clients.isEmpty()) {
						validateVehicles(clients);
					}
					return;
				}
				
			}
		}
		
	}




	private void validateModels(List<Long> models) throws UnableToProccessIteamException {

		Set<Long> uniqueModels = new HashSet<>(models);

		String hql = "SELECT COUNT(m.id) FROM Model m WHERE m.tenantId = :tenantId AND m.id IN :modelIds";
		
		Query query = entityManager.createQuery(hql);
		query.setParameter("tenantId", TenantContext.getTenantId());
		query.setParameter("modelIds", uniqueModels);
		long number = (long) query.getSingleResult();
		
		entityManager.clear();
		
		if (number != uniqueModels.size()) {
			throw new UnableToProccessIteamException(VehiculeChecker.ERRORMESSAGE);
		}
	}


	private void validateBrands(List<Long> brands) throws UnableToProccessIteamException {

		Set<Long> uniqueBrands = new HashSet<>(brands) ;
		String hql = "SELECT COUNT(b.id) FROM Brand b WHERE b.tenantId = :tenantId AND b.id IN :brandIds";
		
		Query query = entityManager.createQuery(hql);
		query.setParameter("tenantId", TenantContext.getTenantId());
		query.setParameter("brandIds", uniqueBrands);
		long number = (long) query.getSingleResult();
		
		entityManager.clear();
		
		if (number != uniqueBrands.size()) {
			throw new UnableToProccessIteamException(VehiculeChecker.ERRORMESSAGE);
		}
	}


	private void validateVehicles(List<Long> vehicules) throws UnableToProccessIteamException {

		Set<Long> uniquevehicules = new HashSet<>(vehicules);

		String hql = "SELECT COUNT(v.id) FROM Vehicule v WHERE v.id IN :vehicluesIds";
		
		Query query = entityManager.createQuery(hql);
		query.setParameter("vehicluesIds", uniquevehicules);
		long number = (long) query.getSingleResult();
		
		entityManager.clear();
		
		if (number != uniquevehicules.size()) {
			throw new UnableToProccessIteamException(VehiculeChecker.ERRORMESSAGEVEHICLUE);
		}
	}
}
