package ma.crm.carental.repositories.interfaces;

import java.util.List;

import ma.crm.carental.entities.Charge;

public interface ChargeInterface {

    List<Charge> insertChargeInBatch(List<Charge> charges) ;

    int deleteCharges(List<Long> chargesIds ) ;

    int updateChargesInBatch(List<Long> chargeIds , Charge charge) ;

    List<Charge> chargesWithPagination(int page, int pageSize) ;

    Charge find(long id) ;

    Long count() ;
    
}
