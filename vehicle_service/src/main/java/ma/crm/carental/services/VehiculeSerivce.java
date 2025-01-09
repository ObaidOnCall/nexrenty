package ma.crm.carental.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import ma.crm.carental.annotations.ValidateVehiculeModels;
import ma.crm.carental.dtos.vehicule.AssuranceRequestDto;
import ma.crm.carental.dtos.vehicule.AssuranceResponseDto;
import ma.crm.carental.dtos.vehicule.VehRequsetDto;
import ma.crm.carental.dtos.vehicule.VehResponseDto;
import ma.crm.carental.entities.Assurance;
import ma.crm.carental.entities.Vehicule;
import ma.crm.carental.exception.UnableToProccessIteamException;
import ma.crm.carental.mappers.VehiculeMapper;
import ma.crm.carental.repositories.AssuranceRepo;
import ma.crm.carental.repositories.VehiculeRepo;
import ma.crm.carental.tenantfilter.TenantContext;
import ma.crm.carental.utils.JsonConverter;




@Service
@Transactional
public class VehiculeSerivce {
    
	private static final String ERRORMESSAGE1 = "access denied or unable to process the item within vhicule,please use a valid data" ;


    private final VehiculeRepo vehiculeRepo ;
    private final AssuranceRepo assuranceRepo ;
    private final VehiculeMapper vehiculeMapper ;
    private final JsonConverter jsonConverter ;


    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public VehiculeSerivce (
            VehiculeRepo vehiculeRepo , 
            VehiculeMapper vehiculeMapper ,
            AssuranceRepo assuranceRepo ,
            JsonConverter jsonConverter 
        ) {
        this.vehiculeRepo = vehiculeRepo ;
        this.vehiculeMapper = vehiculeMapper ;
        this.assuranceRepo = assuranceRepo ;
        this.jsonConverter = jsonConverter ;
    }

    @ValidateVehiculeModels
    public List<VehResponseDto> saveVehs(List<VehRequsetDto> vehrequests) {

        List<Vehicule> vehicules = vehiculeMapper.toVeh(vehrequests) ;
        vehiculeRepo.saveAllAndFlush(vehicules) ;

        entityManager.clear();
        return vehiculeMapper.fromVeh(vehicules) ;

    }

    public Page<Vehicule> vehsPaginate(int page , int limt) {
        return vehiculeRepo.findAll(PageRequest.of(page, limt)) ;
    }


    public Map<String , Object> deleteVehs(List<Long> ids) throws UnableToProccessIteamException {

        Map<String , Object> response = new HashMap<>() ;
        try {
            
            vehiculeRepo.deleteVehiculesByIdsAndTenantId(ids , TenantContext.getTenantId());
    
    
            response.put("message", "deleted secuufly") ;
            response.put("code", 14) ;
        } catch (DataIntegrityViolationException e) {
            throw new UnableToProccessIteamException("Unable to process the vehicle items. Please check your request data") ;
        }

        return response ;
    }

    @ValidateVehiculeModels
    public Map<String , Object> updateVehs(List<VehRequsetDto> vehRequsetDtos , List<Long> ids) {

        int count = vehiculeRepo.updateVehiculesInBatch(
                                                        ids,
                                                        TenantContext.getTenantId() ,
                                                        vehRequsetDtos.get(0).getMatricule(),
                                                        vehRequsetDtos.get(0).getModel(), 
                                                        vehRequsetDtos.get(0).getPrice()
                                                        // jsonConverter.convertToDatabaseColumn(vehRequsetDtos.get(0).getMetadata()) 
                                                    ) ;
        
        Map<String , Object> response = new HashMap<>() ;
        response.put("message",count + " updated recodrds") ;
        response.put("code" , 5) ;
        return  response;
    }
    
    
    public VehResponseDto showVeh(Long id) {

        Optional<Vehicule> vehicule = vehiculeRepo.findByIdAndTenantId(id , TenantContext.getTenantId()) ;
        
        if (!vehicule.isPresent()) {
            throw new UnableToProccessIteamException(VehiculeSerivce.ERRORMESSAGE1) ;
        }

        List<Vehicule> vehicules = new ArrayList<>() ;
        vehicules.add(vehicule.get()) ;

        return vehiculeMapper.fromVeh(vehicules).get(0) ;
    }
    /**
     * @apiNote assurance sevice fn
     */


    public List<AssuranceResponseDto> saveAssurances(List<AssuranceRequestDto> assuranceRequestDtos) {

        List<Assurance> assurances = vehiculeMapper.toAssurances(assuranceRequestDtos) ;
        assuranceRepo.saveAllAndFlush(assurances) ;

        entityManager.clear();
        return vehiculeMapper.fromAssurances(assurances) ;

    }
}
