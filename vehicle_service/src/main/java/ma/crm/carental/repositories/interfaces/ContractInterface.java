package ma.crm.carental.repositories.interfaces;

import java.util.List;

import ma.crm.carental.entities.Contract;

public interface ContractInterface {

    List<Contract> insertContractInBatch(List<Contract> contracts) ;

    int deleteContracts(List<Long> contractIds ) ;

    int updateContractsInBatch(List<Long> contractIds , Contract contract) ;

    List<Contract> contractsWithPagination(int page, int pageSize) ;

    Contract find(long id) ;

    Long count() ;
    
}
