package ma.crm.carental.mappers;

import java.util.List;
import java.util.stream.Collectors;


import org.springframework.stereotype.Component;

import ma.crm.carental.dtos.client.ClientResponseDto;
import ma.crm.carental.dtos.contract.ContractRequestDto;
import ma.crm.carental.dtos.contract.ContractResponseDto;
import ma.crm.carental.dtos.deliveryguy.DeliveryGuyResponseDto;
import ma.crm.carental.dtos.vehicule.ModelResponseDto;
import ma.crm.carental.dtos.vehicule.VehResponseDto;
import ma.crm.carental.entities.Client;
import ma.crm.carental.entities.Contract;
import ma.crm.carental.entities.DeliveryGuy;
import ma.crm.carental.entities.Vehicule;
import ma.crm.carental.utils.AuthUtiles;


@Component
public class ContractMapper {


    public List<Contract> toContract(List<ContractRequestDto> contractRequestDtos) {
        
        String createdBy = AuthUtiles.getUsername() ;
        return contractRequestDtos.stream()
                                .map(contractRequestDto ->
                                            Contract.builder()
                                            .vehicule(
                                                Vehicule.builder()
                                                .id(contractRequestDto.getVehicule())
                                                .build()
                                            )
                                            .clientId(contractRequestDto.getClient())
                                            .deliveryGuy(
                                                DeliveryGuy.builder()
                                                .id(contractRequestDto.getDeliveryGuy())
                                                .build()
                                            )
                                            .numContract(contractRequestDto.getNumContract())
                                            .caution(contractRequestDto.getCaution())
                                            .days(contractRequestDto.getDays())
                                            .startDate(contractRequestDto.getStartDate())
                                            .finDate(contractRequestDto.getFinDate())
                                            .deliveryCosts(contractRequestDto.getDeliveryCosts())
                                            .price(contractRequestDto.getPrice())
                                            .preGivenPrice(contractRequestDto.getPreGivenPrice())
                                            .remainingPrice(contractRequestDto.getRemainingPrice())
                                            .placeOfContract(contractRequestDto.getPlaceOfContract())
                                            .placeOfDelivery(contractRequestDto.getPlaceOfDelivery())
                                            .placeOfReturn(contractRequestDto.getPlaceOfReturn())
                                            .dateValideDrivingLicence(contractRequestDto.getDateValideDrivingLicence())
                                            .createdBy(createdBy)
                                            
                                            .build() 
                ).toList() ;

    }
    

    public List<ContractResponseDto> fromContract(List<Contract> contracts) {

        return contracts.stream().map(
            contract ->ContractResponseDto.builder()
                                    .id(contract.getId())
                                    .vehicule(
                                        VehResponseDto.builder()
                                        .id(contract.getVehicule().getId())
                                        .matricule(contract.getVehicule().getMatricule())
                                        // .model(
                                        //     ModelResponseDto.builder()
                                        //     .id(contract.getVehicule().getModel().getId())
                                        //     .name(contract.getVehicule().getModel().getName())
                                        //     .topSpeed(contract.getVehicule().getModel().getTopSpeed())
                                        //     .numberOfDoors(contract.getVehicule().getModel().getNumberOfDoors())
                                        //     .build()
                                        // )
                                        .build()
                                    )
                                    .client(contract.getClientId())
                                    .numContract(contract.getNumContract())
                                    .caution(contract.getCaution())
                                    .days(contract.getDays())
                                    .startDate(contract.getStartDate())
                                    .finDate(contract.getFinDate())
                                    .deliveryCosts(contract.getDeliveryCosts())
                                    .price(contract.getPrice())
                                    .preGivenPrice(contract.getPreGivenPrice())
                                    .remainingPrice(contract.getRemainingPrice())
                                    .placeOfContract(contract.getPlaceOfContract())
                                    .placeOfDelivery(contract.getPlaceOfDelivery())
                                    .placeOfReturn(contract.getPlaceOfReturn())
                                    .dateValideDrivingLicence(contract.getDateValideDrivingLicence())
                                    .build()
        ).toList() ;
    }
}
