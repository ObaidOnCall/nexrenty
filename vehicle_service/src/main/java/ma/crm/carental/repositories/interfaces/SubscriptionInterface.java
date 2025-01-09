package ma.crm.carental.repositories.interfaces;

import java.util.List;

import ma.crm.carental.entities.Subscription;
import ma.crm.carental.entities.SubscriptionType;


public interface SubscriptionInterface {

    List<Subscription> insertSubscriptionInBatch(List<Subscription> subscriptions) ;

    int deleteSubscriptions(List<Long> subscriptionsIds ) ;

    int updateSubscriptionsInBatch(List<Long> subscriptionIds , Subscription subscription) ;

    List<Subscription> subscriptionsWithPagination(int page, int pageSize) ;

    List<SubscriptionType> subscriptionsTypes() ;

    Subscription find(String id) ;

    Long count() ;
}