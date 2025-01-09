package ma.crm.carental.repositories.interfaces;

import java.util.List;

import ma.crm.carental.entities.DeliveryGuy;

public interface DeliveryGuyInterface {

    List<DeliveryGuy> insertDeliveryGuyInBatch(List<DeliveryGuy> deliveryGuys) ;

    int deleteDeliveryGuys(List<Long> deliveryGuyIds ) ;

    int updateDeliveryGuysInBatch(List<Long> deliveryGuyIds , DeliveryGuy deliveryGuy) ;

    List<DeliveryGuy> deliveryGuysWithPagination(int page, int pageSize) ;

    DeliveryGuy find(long id) ;

    Long count() ;
    
}
