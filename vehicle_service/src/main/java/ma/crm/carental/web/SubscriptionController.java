package ma.crm.carental.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ma.crm.carental.dtos.interfaces.validationgroups.CreateValidationGroup;
import ma.crm.carental.dtos.subscription.SubscriptionRequestDto;
import ma.crm.carental.dtos.subscription.SubscriptionResponseDto;
import ma.crm.carental.entities.SubscriptionType;
import ma.crm.carental.services.SubscriptionService;

@RestController
@RequestMapping("/subscriptions")
@Validated
public class SubscriptionController {
    
    private final SubscriptionService subscriptionService ;


    @Autowired
    SubscriptionController(
        SubscriptionService subscriptionService
    ) {
        this.subscriptionService = subscriptionService ;
    }


    @PostMapping
    @Validated(CreateValidationGroup.class)
    List<SubscriptionResponseDto> save(
        @RequestBody @Valid List<SubscriptionRequestDto> subscriptionRequestDtos
    ) {

        return subscriptionService.saveSubscriptions(subscriptionRequestDtos) ;
    }



    @GetMapping("/{id}")
    SubscriptionResponseDto find(
        @PathVariable String id 
    ) {
        return subscriptionService.findSubscription(id) ;
    }

    @GetMapping("/types")
    public List<SubscriptionType> getAllSubscriptionsTypes() {

        return subscriptionService.getAllSubscriptionsTypes() ;
    }
}
