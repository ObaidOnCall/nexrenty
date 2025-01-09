package ma.crm.carental.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import ma.crm.carental.annotations.ValidateClients;
import ma.crm.carental.annotations.ValidateDeliveryGuys;
import ma.crm.carental.annotations.ValidateVehicules;
import ma.crm.carental.dtos.contract.ContractRequestDto;
import ma.crm.carental.dtos.contract.ContractResponseDto;
import ma.crm.carental.entities.Contract;
import ma.crm.carental.exception.UnableToProccessIteamException;
import ma.crm.carental.mappers.ContractMapper;
import ma.crm.carental.repositories.ContractRepo;

@Service
@Transactional
public class ContractService {

    private static final String ERRORMESSAGE = "Access denied or unable to process the item within the contracts. Contract ID: ";

    private final ContractRepo contractRepo ;

    private final ContractMapper contractMapper ;

    @Autowired
    ContractService (
        ContractRepo contractRepo ,
        ContractMapper contractMapper
    ) {
        this.contractRepo = contractRepo ;
        this.contractMapper = contractMapper ;
    }
    
    @ValidateClients
    @ValidateVehicules
    @ValidateDeliveryGuys
    public List<ContractResponseDto> saveContracts(List<ContractRequestDto> contractRequestDtos){

        return contractMapper.fromContract(
                                contractRepo.insertContractInBatch(
                                            contractMapper.toContract(contractRequestDtos)
                ));
    }

    public Map<String , Object> deleteContracts(List<Long> dguysIds) {

        Map<String , Object> serviceMessage = new HashMap<>() ;
        
        int count = contractRepo.deleteContracts(dguysIds) ;

        serviceMessage.put("status", true) ;
        serviceMessage.put("message", String.format("Number Of Deleted Contracts is %d", count)) ;
        return serviceMessage ;
        
    }


    @ValidateClients
    @ValidateDeliveryGuys
    @ValidateVehicules
    public Map<String , Object> updateContracts(List<Long> contractsIds , List<ContractRequestDto> contractRequestDtos) {

        Map<String , Object> serviceMessage = new HashMap<>() ;


        int count = contractRepo.updateContractsInBatch(contractsIds, contractMapper.toContract(contractRequestDtos).get(0)) ;

        serviceMessage.put("status", true) ;
        serviceMessage.put("message", "Number Of Updated Contracts is " + count) ;

        return serviceMessage ;
    }



    public Page<ContractResponseDto> pagenateContracts(Pageable pageable) {
        
        Long totalElements = contractRepo.count() ;


        List<ContractResponseDto> contractResponseDtos = contractMapper.fromContract(
            contractRepo.contractsWithPagination(pageable.getPageNumber(), pageable.getPageSize())
        ) ;

        return new PageImpl<>(contractResponseDtos , pageable , totalElements) ;
    }

    
    public ContractResponseDto findContract(long id) {

        
        try {
            Contract contract = contractRepo.find(id) ;

            /**
             * @convert the deliveryGuy object to List of deliveryGuy to use the general mapper
             */
            List<Contract> contracts = List.of(contract) ;

            return contractMapper.fromContract(contracts).get(0) ;

        } catch (NoResultException e) {
            throw new UnableToProccessIteamException(ERRORMESSAGE + id) ;
        }
    }
}
