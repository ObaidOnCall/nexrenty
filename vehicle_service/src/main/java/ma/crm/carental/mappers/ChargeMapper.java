package ma.crm.carental.mappers;

import java.util.List;
import java.util.stream.Collectors;


import org.springframework.stereotype.Component;

import ma.crm.carental.dtos.charge.ChargeRequestDto;
import ma.crm.carental.dtos.charge.ChargeResponseDto;
import ma.crm.carental.dtos.contract.ContractResponseDto;
import ma.crm.carental.entities.Charge;
import ma.crm.carental.entities.Contract;
import ma.crm.carental.utils.AuthUtiles;


@Component
public class ChargeMapper {


    public List<Charge> toCharge(List<ChargeRequestDto> chargeRequestDtos) {

        String createdBy = AuthUtiles.getUsername() ;
        return chargeRequestDtos.stream()
                                .map(chargeRequestDto ->
                                            Charge.builder()
                                            .contract(
                                                Contract.builder()
                                                .id(chargeRequestDto.getContract())
                                                .build()
                                            )
                                            .amount(chargeRequestDto.getAmount())
                                            .changeType(chargeRequestDto.getChangeType())
                                            .isPaid(chargeRequestDto.getIsPaid())
                                            .descrption(chargeRequestDto.getDescription())
                                            .createdBy(createdBy)
                                            .build() 
                ).toList() ;

    }
    

    public List<ChargeResponseDto> fromCharge(List<Charge> charges) {

        return charges.stream().map(
            charge ->ChargeResponseDto.builder()
                                    .id(charge.getId())
                                    .contract(
                                        ContractResponseDto.builder()
                                        .id(charge.getContract().getId())
                                        .days(charge.getContract().getDays())
                                        .finDate(charge.getContract().getFinDate())
                                        .build()
                                    )
                                    .amount(charge.getAmount())
                                    .isPaid(charge.getIsPaid())
                                    .descrption(charge.getDescrption())
                                    .changeType(charge.getChangeType())
                                    .createdBy(charge.getCreatedBy())
                                    .createdAt(charge.getCreatedAt())
                                    .updatedAt(charge.getUpdatedAt())
                                    .build()
        ).collect(Collectors.toList()) ;
    }
}
