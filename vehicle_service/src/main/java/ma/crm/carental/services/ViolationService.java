package ma.crm.carental.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import ma.crm.carental.annotations.ValidateClients;
import ma.crm.carental.dtos.violation.ViolationRequestDto;
import ma.crm.carental.dtos.violation.ViolationResponseDto;
import ma.crm.carental.entities.Violation;
import ma.crm.carental.exception.UnableToProccessIteamException;
import ma.crm.carental.mappers.ViolationMapper;
import ma.crm.carental.repositories.ViolationRepo;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@Service
@Transactional
public class ViolationService {
    

	private static final String ERRORMESSAGE = "access denied or unable to process the item within the violation" ;


    private final ViolationRepo violationRepo ;
    private final ViolationMapper violationMapper ;


    @Autowired
    ViolationService(
        ViolationRepo violationRepo ,
        ViolationMapper violationMapper
    ){
        this.violationRepo = violationRepo ;
        this.violationMapper = violationMapper ;
    }
    
    @ValidateClients
    public List<ViolationResponseDto> saveViolations (
        List<ViolationRequestDto> violationRequestDtos
    ) {
        
        List<Violation> violations =  violationRepo.insertViolationInBatch(
            violationMapper.toViolation(violationRequestDtos)
        );

        return violationMapper.fromViolation(violations) ;
    }


    public Map<String , Object> deleteViolations(List<Long> violationsIds) {

        Map<String , Object> serviceMessage = new HashMap<>() ;
        
        int count = violationRepo.deleteViolations(violationsIds) ;

        serviceMessage.put("status", true) ;
        serviceMessage.put("message", "Number Of Deleted Client is :" + count) ;

        return serviceMessage ;
        
    }

    @ValidateClients
    public Map<String , Object> updateViolations(List<Long> violationIds , List<ViolationRequestDto> violationRequestDtos) {

        Map<String , Object> serviceMessage = new HashMap<>() ;


        int count = violationRepo.updateViolationsInBatch(violationIds, violationMapper.toViolation(violationRequestDtos).get(0)) ;

        serviceMessage.put("status", true) ;
        serviceMessage.put("message", "Number Of Updated Clients is :" + count) ;

        return serviceMessage ;
    }


    public Page<ViolationResponseDto> pagenateViolations(Pageable pageable) {

        Long totalElements = violationRepo.count() ;

        List<ViolationResponseDto> violationResponseDtos = violationMapper.fromViolation(
            violationRepo.volationsWithPagination(pageable.getPageNumber(), pageable.getPageSize())
        ) ;

        return new PageImpl<>(violationResponseDtos , pageable , totalElements) ;
    }



    public ViolationResponseDto findViolation(long id) {

        
        try {
            Violation violation = violationRepo.find(id) ;

            /**
             * @convert the Violation object to List of Violation to use the general mapper
             */
            List<Violation> violations = List.of(violation) ;

            return violationMapper.fromViolation(violations).get(0) ;

        } catch (NoResultException e) {
            throw new UnableToProccessIteamException(ViolationService.ERRORMESSAGE) ;
        }
    }
}
