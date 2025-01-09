package ma.crm.carental.repositories.interfaces;

import java.util.List;

import ma.crm.carental.entities.Violation;


/**
 * ViolationRepoInterface
 */
public interface ViolationRepoInterface {

    List<Violation> insertViolationInBatch(List<Violation> violations) ;

    int deleteViolations(List<Long> violationsIds ) ;

    int updateViolationsInBatch(List<Long> violationsIds , Violation violation) ;

    List<Violation> volationsWithPagination(int page, int pageSize) ;

    Violation find(long id) ;

    Long count() ;
    
}
