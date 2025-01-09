package ma.crm.carental.mappers;

import java.util.List;
import java.util.stream.Collectors;


import org.springframework.stereotype.Component;


import ma.crm.carental.dtos.reservation.ReservationRequestDto;
import ma.crm.carental.dtos.reservation.ReservationResponseDto;
import ma.crm.carental.dtos.subscription.SubscriptionRequestDto;
import ma.crm.carental.dtos.subscription.SubscriptionResponseDto;
import ma.crm.carental.dtos.vehicule.VehResponseDto;
import ma.crm.carental.entities.Reservation;
import ma.crm.carental.entities.Subscription;
import ma.crm.carental.entities.SubscriptionType;
import ma.crm.carental.entities.Vehicule;


@Component
public class SubscriptionMapper {


    public List<Subscription> toSubscription(List<SubscriptionRequestDto> subscriptionRequestDtos) {

        return subscriptionRequestDtos.stream()
                                .map(subscriptionRequestDto ->
                                           Subscription.builder()
                                           .subscriptionType(
                                            SubscriptionType.builder()
                                            .id(subscriptionRequestDto.getSubscriptionTypeId().toString())
                                            .build()
                                           )
                                           .build()

                ).collect(Collectors.toList()) ;

    }
    

    public List<SubscriptionResponseDto> fromSubscription(List<Subscription> subscriptions) {

        return subscriptions.stream().map(
            subscription ->  SubscriptionResponseDto.builder()
                            .id(subscription.getId())
                            .subscriptionType(
                                SubscriptionType.builder()
                                .maxBrands(subscription.getSubscriptionType().getMaxBrands())
                                .maxVehicles(subscription.getSubscriptionType().getMaxVehicles())
                                .maxCharges(subscription.getSubscriptionType().getMaxCharges())
                                .maxClients(subscription.getSubscriptionType().getMaxClients())
                                .maxDeliveries(subscription.getSubscriptionType().getMaxDeliveries())
                                .maxContracts(subscription.getSubscriptionType().getMaxContracts())
                                .maxReservations(subscription.getSubscriptionType().getMaxReservations())
                                .maxViolations(subscription.getSubscriptionType().getMaxViolations())
                                .build()
                            )
                            .build()
        ).collect(Collectors.toList()) ;
    }
}
