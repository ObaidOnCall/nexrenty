package ma.crm.carental.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;


import ma.crm.carental.dtos.deliveryguy.DeliveryGuyRequestDto;
import ma.crm.carental.dtos.deliveryguy.DeliveryGuyResponseDto;
import ma.crm.carental.entities.DeliveryGuy;



@Component
public class DeliveryGuyMapper {
    

    public List<DeliveryGuy> toDeliveryGuy(List<DeliveryGuyRequestDto> deliveryGuyRequestDtos) {


        return deliveryGuyRequestDtos.stream()
                                .map(deliveryGuydto ->
                                            DeliveryGuy.builder()
                                            .firstname(deliveryGuydto.getFirstname())
                                            .lastname(deliveryGuydto.getLastname())
                                            .address(deliveryGuydto.getAddress())
                                            .email(deliveryGuydto.getEmail())
                                            .ville(deliveryGuydto.getVille())
                                            .nationality(deliveryGuydto.getNationality())
                                            .cinOrPassport(deliveryGuydto.getCinOrPassport())
                                            .cinIsValideUntil(deliveryGuydto.getCinIsValideUntil())
                                            .phone1(deliveryGuydto.getPhone1())
                                            .phone2(deliveryGuydto.getPhone2())
                                            .codePostal(deliveryGuydto.getCodePostal())
                                            .build() 
                ).collect(Collectors.toList()) ;

    }


    public List<DeliveryGuyResponseDto> fromDeliveryGuy(List<DeliveryGuy> deliveryGuys) {

        return deliveryGuys.stream().map(
            deliveryGuy ->DeliveryGuyResponseDto.builder()
                                    .id(deliveryGuy.getId())
                                    .firstname(deliveryGuy.getFirstname())
                                    .lastname(deliveryGuy.getLastname())
                                    .email(deliveryGuy.getEmail())
                                    .codePostal(deliveryGuy.getCodePostal())
                                    .address(deliveryGuy.getAddress())
                                    .cinOrPassport(deliveryGuy.getCinOrPassport())
                                    .cinIsValideUntil(deliveryGuy.getCinIsValideUntil())
                                    .licence(deliveryGuy.getLicence())
                                    .licenceIsValideUntil(deliveryGuy.getLicenceIsValideUntil())
                                    .ville(deliveryGuy.getVille())
                                    .phone1(deliveryGuy.getPhone1())
                                    .phone2(deliveryGuy.getPhone2())
                                    .build()
        ).collect(Collectors.toList()) ;
    }
}
