package ma.crm.carental.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import ma.crm.carental.annotations.ValidateContracts;
import ma.crm.carental.dtos.charge.ChargeRequestDto;
import ma.crm.carental.dtos.charge.ChargeResponseDto;
import ma.crm.carental.entities.Charge;
import ma.crm.carental.exception.UnableToProccessIteamException;
import ma.crm.carental.mappers.ChargeMapper;
import ma.crm.carental.repositories.ChargeRepo;

@Service
@Transactional
public class ChargeService {

    private static final String ERRORMESSAGE = "Access denied or unable to process the item within the Charges. Charge ID: ";

    private final ChargeMapper chargeMapper ;
    private final ChargeRepo chargeRepo ;

    ChargeService(
        ChargeMapper chargeMapper ,
        ChargeRepo chargeRepo
    ) {
        this.chargeMapper = chargeMapper ;
        this.chargeRepo = chargeRepo ;
    }

    
    @ValidateContracts
    public List<ChargeResponseDto> saveCharges(List<ChargeRequestDto> chargeRequestDtos){

        return chargeMapper.fromCharge(
                                chargeRepo.insertChargeInBatch(
                                            chargeMapper.toCharge(chargeRequestDtos)
                ));
    }

    public Map<String , Object> deleteCharges(List<Long> chargesIds) {

        Map<String , Object> serviceMessage = new HashMap<>() ;
        
        int count = chargeRepo.deleteCharges(chargesIds) ;

        serviceMessage.put("status", true) ;
        serviceMessage.put("message", String.format("Number Of Deleted Charges is %d", count)) ;
        return serviceMessage ;
        
    }
    
    @ValidateContracts
    public Map<String , Object> updateCharges(List<Long> chargesIds , List<ChargeRequestDto> chargeRequestDtos) {

        Map<String , Object> serviceMessage = new HashMap<>() ;

        int count = chargeRepo.updateChargesInBatch(chargesIds, chargeMapper.toCharge(chargeRequestDtos).get(0)) ;

        serviceMessage.put("status", true) ;
        serviceMessage.put("message", "Number Of Updated Charges is " + count) ;

        return serviceMessage ;
    }

    public Page<ChargeResponseDto> pagenateCharges(Pageable pageable) {
        
        Long totalElements = chargeRepo.count() ;


        List<ChargeResponseDto> chargeResponseDtos = chargeMapper.fromCharge(
            chargeRepo.chargesWithPagination(pageable.getPageNumber(), pageable.getPageSize())
        ) ;

        return new PageImpl<>(chargeResponseDtos , pageable , totalElements) ;
    }

    public ChargeResponseDto findCharge(long id) {

        
        try {
            Charge charge = chargeRepo.find(id) ;

            List<Charge> charges = List.of(charge) ;

            return chargeMapper.fromCharge(charges).get(0) ;

        } catch (NoResultException e) {
            throw new UnableToProccessIteamException(ERRORMESSAGE + id) ;
        }
    }
}
