package ma.crm.carental.services;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import ma.crm.carental.dtos.subscription.SubscriptionRequestDto;
import ma.crm.carental.dtos.subscription.SubscriptionResponseDto;
import ma.crm.carental.entities.Subscription;
import ma.crm.carental.entities.SubscriptionType;
import ma.crm.carental.exception.UnableToProccessIteamException;
import ma.crm.carental.mappers.SubscriptionMapper;
import ma.crm.carental.repositories.SubscriptionRepo;

@Service
@Transactional
public class SubscriptionService {
    
    private static final String ERRORMESSAGE = "Access denied or unable to process the item within the Subscription. Subscription ID: ";

    private final SubscriptionRepo subscriptionRepo ;
    private final SubscriptionMapper subscriptionMapper ;

    SubscriptionService (
        SubscriptionRepo subscriptionRepo ,
        SubscriptionMapper subscriptionMapper
    ) {
        this.subscriptionRepo = subscriptionRepo ;
        this.subscriptionMapper = subscriptionMapper ;
    }

    public List<SubscriptionResponseDto> saveSubscriptions(List<SubscriptionRequestDto> subscriptionRequestDtos){

        return subscriptionMapper.fromSubscription(
                                subscriptionRepo.insertSubscriptionInBatch(
                                            subscriptionMapper.toSubscription(subscriptionRequestDtos)
                ));
    }


    public SubscriptionResponseDto findSubscription(String id) {

        try {
            Subscription subscription = subscriptionRepo.find(id) ;

            List<Subscription> subscriptions = List.of(subscription) ;

            return subscriptionMapper.fromSubscription(subscriptions).get(0) ;

        } catch (NoResultException e) {
            throw new UnableToProccessIteamException(ERRORMESSAGE + id) ;
        }
    }

    public List<SubscriptionType> getAllSubscriptionsTypes() {

        return subscriptionRepo.subscriptionsTypes() ;
    }
}
