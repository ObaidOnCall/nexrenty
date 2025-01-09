package ma.crm.carental.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import ma.crm.carental.dtos.client.ClientRequestDto;
import ma.crm.carental.dtos.client.ClientResponseDto;
import ma.crm.carental.dtos.violation.ViolationRequestDto;
import ma.crm.carental.dtos.violation.ViolationResponseDto;
import ma.crm.carental.entities.Charge;
import ma.crm.carental.entities.Client;
import ma.crm.carental.entities.Violation;

@Component
public class ViolationMapper {
    

    public List<Violation> toViolation(List<ViolationRequestDto> violationRequestDtos) {


        return violationRequestDtos.stream()
                                .map(violationRequestDto ->
                                            Violation.builder()
                                            .description(violationRequestDto.getDescription())
                                            .finAmount(violationRequestDto.getFinAmount())
                                            .isPaid(violationRequestDto.isPaid())
                                            .clientId(violationRequestDto.getClient())
                                            .charge(
                                                violationRequestDto.getCharge() != null && violationRequestDto.getCharge() != 0 
                                                ? Charge.builder().id(violationRequestDto.getCharge()).build() 
                                                : null
                                            )
                                            .date(violationRequestDto.getDate())
                                            .build()
                ).toList() ;

    }

    public List<ViolationResponseDto> fromViolation(List<Violation> violations) {

        // .client(
        //     ClientResponseDto.builder()
        //                     .firstname(violation.getClient().getFirstname())
        //                     .lastname(violation.getClient().getLastname())
        //                     .id(violation.getClient().getId())
        //                     .build()
        // )
        return violations.stream().map(
            violation ->    ViolationResponseDto.builder()
                            .id(violation.getId())
                            .description(violation.getDescription())
                            .finAmount(violation.getFinAmount())
                            .isPaid(violation.getIsPaid())
                            .client(violation.getClientId())
                            .date(violation.getDate()) 
                            .build()
        ).toList();
    }
}
