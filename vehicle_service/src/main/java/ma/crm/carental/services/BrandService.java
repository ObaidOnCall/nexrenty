package ma.crm.carental.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import ma.crm.carental.annotations.ValidateVehiculeBrands;
import ma.crm.carental.dtos.vehicule.BrandRequsetDto;
import ma.crm.carental.dtos.vehicule.BrandResponseDto;
import ma.crm.carental.dtos.vehicule.ModelRequestDto;
import ma.crm.carental.dtos.vehicule.ModelResponseDto;
import ma.crm.carental.dtos.vehicule.ModelUpdateRequestDto;
import ma.crm.carental.entities.Brand;
import ma.crm.carental.entities.Model;
import ma.crm.carental.exception.UnableToProccessIteamException;
import ma.crm.carental.mappers.BrandMapper;
import ma.crm.carental.repositories.BrandRepo;
import ma.crm.carental.repositories.ModelRepo;
import ma.crm.carental.tenantfilter.TenantContext;

@Service
@Transactional
public class BrandService {
    

    private final BrandMapper brandMapper ;
    private final BrandRepo brandRepo ;
    private final ModelRepo modelRepo ;

    @PersistenceContext
    private EntityManager entityManager;


    BrandService (BrandRepo brandRepo , BrandMapper brandMapper , ModelRepo modelRepo ) {
        this.brandRepo = brandRepo ;
        this.brandMapper = brandMapper ;
        this.modelRepo = modelRepo ;
    }


    public List<BrandResponseDto> saveBrands(List<BrandRequsetDto> brandRequsetDtos , String tenantID) {

        List<Brand> brands = brandMapper.toBrand(brandRequsetDtos , tenantID) ;
        brandRepo.saveAllAndFlush(brands) ;

        entityManager.clear();

        return brandMapper.fromBrand(brands) ;

    }

    public List<BrandResponseDto> listBrands() {

        return brandMapper.fromBrand(brandRepo.findAll()) ;
    }

    public BrandResponseDto showBrand(Long id) throws UnableToProccessIteamException {

        Optional<Brand> optionalBrand = brandRepo.findByIdAndTenantId(id , TenantContext.getTenantId());
    
        if (optionalBrand.isPresent()) {
            return brandMapper.fromOneBrand(optionalBrand.get());
        } else {
            // Handle the case where the brand is not found, e.g., throw an exception or return a default value
            throw new UnableToProccessIteamException("Brand not found with id: " + id);
        }
    }



    /***
     * @apiNote Models
     * @param modelRequestDtos
     * @param ownerID
     * @return
     */
    @ValidateVehiculeBrands
    public List<ModelResponseDto> saveModels(List<ModelRequestDto> modelRequestDtos) {

        List<Model> models = brandMapper.toModel(modelRequestDtos) ;

        return brandMapper.fromModel(modelRepo.saveAllAndFlush(models)) ;

    }


    /**
     * @throws UnableToProccessIteamException 
     * @ get the models of brand
     */

    public List<ModelResponseDto> listModels(Long id ) throws UnableToProccessIteamException {

        Optional<Brand> optionalBrand = brandRepo.findByIdAndTenantId(id , TenantContext.getTenantId());
    
        if (optionalBrand.isPresent()) {
            return brandMapper.fromModel(optionalBrand.get().getModels());
        } else {
            // Handle the case where the brand is not found, e.g., throw an exception or return a default value
            throw new UnableToProccessIteamException("Brand not found with id: " + id);
        }
    }

    /**
     * @we use List<ModelRequestDto> instead of ModelRequestDto object for the VehChecker
     * @see ma.crm.carental.aspectj.VehiculeChecker.validateVehicleBrands() pointcut
     * @param modelRequestDtos
     * @param ids
     * @return
     */
    @ValidateVehiculeBrands
    public Map<String , Object> updateModels(List<ModelUpdateRequestDto> modelRequestDtos , Long[] ids) {
        int count1 = 0;
        int count2 = 0;
        if (modelRequestDtos.get(0).isParamsHightPriorityPresent()) {
            count1 = modelRepo.updateModelsInBatch(ids, 
                                                    TenantContext.getTenantId(),
                                                    modelRequestDtos.get(0).getBrand() ,
                                                    modelRequestDtos.get(0).getName(),
                                                    modelRequestDtos.get(0).getTopSpeed(),
                                                    modelRequestDtos.get(0).getNumberOfDoors());
        }
        else if (modelRequestDtos.get(0).isParamsLowPriorityPresent()){
            // do somthing
        }

        Map<String , Object> response = new HashMap<>() ;
        response.put("message",(count1 != 0 ? count1 : count2)+ " updated recodrds") ;
        response.put("code" , 15) ;
        return  response;
    }
}
