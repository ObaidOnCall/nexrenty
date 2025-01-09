package ma.crm.carental.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.NoResultException;
import ma.crm.carental.dtos.deliveryguy.DeliveryGuyRequestDto;
import ma.crm.carental.dtos.deliveryguy.DeliveryGuyResponseDto;
import ma.crm.carental.entities.DeliveryGuy;
import ma.crm.carental.exception.UnableToProccessIteamException;
import ma.crm.carental.mappers.DeliveryGuyMapper;
import ma.crm.carental.repositories.DeliveryGuyRepo;


@Service
@Transactional
public class DeliveryGuyService {
    
	private static final String ERRORMESSAGE = "access denied or unable to process the item within the riders" ;



    private final DeliveryGuyRepo deliveryGuyRepo ;
    private final DeliveryGuyMapper deliveryGuyMapper ;

    @Autowired
    DeliveryGuyService(
            DeliveryGuyRepo deliveryGuyRepo ,
            DeliveryGuyMapper deliveryGuyMapper 
        ) {
        this.deliveryGuyRepo = deliveryGuyRepo ;
        this.deliveryGuyMapper = deliveryGuyMapper ;
    }


    public List<DeliveryGuyResponseDto> saveDeliveryGuys(List<DeliveryGuyRequestDto> deliveryGuyRequestDtos){

        return deliveryGuyMapper.fromDeliveryGuy(
                                deliveryGuyRepo.insertDeliveryGuyInBatch(
                                            deliveryGuyMapper.toDeliveryGuy(deliveryGuyRequestDtos)
                            ));
    }

    public Map<String , Object> deleteDeliveryGuys(List<Long> dguysIds) {

        Map<String , Object> serviceMessage = new HashMap<>() ;
        
        int count = deliveryGuyRepo.deleteDeliveryGuys(dguysIds) ;

        serviceMessage.put("status", true) ;
        serviceMessage.put("message", "Number Of Deleted Riders is :" + count) ;

        return serviceMessage ;
        
    }


    public Map<String , Object> updateDeliveryGuys(List<Long> deliveryGuysIds , List<DeliveryGuyRequestDto> deliveryGuyRequestDtos) {

        Map<String , Object> serviceMessage = new HashMap<>() ;


        int count = deliveryGuyRepo.updateDeliveryGuysInBatch(deliveryGuysIds, deliveryGuyMapper.toDeliveryGuy(deliveryGuyRequestDtos).get(0)) ;

        serviceMessage.put("status", true) ;
        serviceMessage.put("message", "Number Of Updated Riders is :" + count) ;

        return serviceMessage ;
    }



    public Page<DeliveryGuyResponseDto> pagenateDeliveryGuys(Pageable pageable) {
        
        Long totalElements = deliveryGuyRepo.count() ;


        List<DeliveryGuyResponseDto> deliveryGuyResponseDtos = deliveryGuyMapper.fromDeliveryGuy(
            deliveryGuyRepo.deliveryGuysWithPagination(pageable.getPageNumber(), pageable.getPageSize())
        ) ;

        return new PageImpl<>(deliveryGuyResponseDtos , pageable , totalElements) ;


    }

    public DeliveryGuyResponseDto findDeliveryGuy(long id) {

        
        try {
            DeliveryGuy deliveryGuy = deliveryGuyRepo.find(id) ;

            /**
             * @convert the deliveryGuy object to List of deliveryGuy to use the general mapper
             */
            List<DeliveryGuy> deliveryGuys = List.of(deliveryGuy) ;

            return deliveryGuyMapper.fromDeliveryGuy(deliveryGuys).get(0) ;

        } catch (NoResultException e) {
            throw new UnableToProccessIteamException(DeliveryGuyService.ERRORMESSAGE) ;
        }
    }

}