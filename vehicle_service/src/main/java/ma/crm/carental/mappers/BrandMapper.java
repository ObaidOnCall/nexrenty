package ma.crm.carental.mappers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import ma.crm.carental.dtos.vehicule.BrandRequsetDto;
import ma.crm.carental.dtos.vehicule.BrandResponseDto;
import ma.crm.carental.dtos.vehicule.ModelRequestDto;
import ma.crm.carental.dtos.vehicule.ModelResponseDto;
import ma.crm.carental.entities.Brand;
import ma.crm.carental.entities.Model;

@Component
public class BrandMapper {
    

    public List<Brand> toBrand (List<BrandRequsetDto> brandRequsetDtos , String tenantId) {

        List<Brand> brands = new ArrayList<>() ;

        for (BrandRequsetDto brandreq : brandRequsetDtos) {

            Brand brand = new Brand() ;
            
            BeanUtils.copyProperties(brandreq, brand);
            /**
             * @@set the ownerID
             */

            brands.add(brand) ;
        }


        return brands ;
    }

    public List<BrandResponseDto> fromBrand (List<Brand> brands) {

        List<BrandResponseDto> brandResponseDtos = new ArrayList<>() ;

        for (Brand brand : brands) {

            BrandResponseDto brandResponseDto = new BrandResponseDto() ;
            BeanUtils.copyProperties(brand, brandResponseDto);
            brandResponseDtos.add(brandResponseDto) ;
        }

        return brandResponseDtos ;
    }


    public BrandResponseDto fromOneBrand (Brand brand) {


        BrandResponseDto brandResponseDto = new BrandResponseDto() ;
        BeanUtils.copyProperties(brand, brandResponseDto);
        
        return brandResponseDto ;
    }

    /**
     * 
     * @param modelRequestDtos
     * @param ownerID
     * @return
     */
    public List<Model> toModel (List<ModelRequestDto> modelRequestDtos) {

        List<Model> models = new ArrayList<>() ;

        for (ModelRequestDto modelreq : modelRequestDtos) {

            Model model = new Model() ;

            model.setBrand(Brand.builder().id(modelreq.getBrand()).build());

            BeanUtils.copyProperties(modelreq, model);
            /**
             * @@set the ownerID
             */

            models.add(model) ;
        }


        return models ;
    }

    public List<ModelResponseDto> fromModel (List<Model> models) {

        List<ModelResponseDto> modelResponseDtos = new ArrayList<>() ;

        for (Model model : models) {

            ModelResponseDto modelResponseDto = new ModelResponseDto() ;
            BrandResponseDto brandResponseDto = new BrandResponseDto() ;

            BeanUtils.copyProperties(model, modelResponseDto);
            BeanUtils.copyProperties(model.getBrand(), brandResponseDto);

            modelResponseDto.setBrand(brandResponseDto);
            
            modelResponseDtos.add(modelResponseDto) ;
        }

        return modelResponseDtos ;
    }


    public ModelResponseDto fromModel (Model model) {



        ModelResponseDto modelResponseDto = new ModelResponseDto() ;
        // BrandResponseDto brandResponseDto = new BrandResponseDto() ;

        BeanUtils.copyProperties(model, modelResponseDto);
        // BeanUtils.copyProperties(model.getBrand(), brandResponseDto);

        // modelResponseDto.setBrand(brandResponseDto);
            
        

        return modelResponseDto ;
    }
}
