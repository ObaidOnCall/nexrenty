package ma.crm.carental.services;

import java.util.Collections;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import ma.crm.carental.dtos.client.ClientResponseDto;

@FeignClient(name = "clients-service")
public interface ClientServiceClient {

    
    @GetMapping("/clients/{ids}")
    @CircuitBreaker(name = "clientServiceBreaker", fallbackMethod = "findClientsFallback")
    List<ClientResponseDto> findClients(@PathVariable List<Long> ids) ;





    default List<ClientResponseDto> findClientsFallback(List<Long> ids, Throwable throwable) {
        
        return Collections.emptyList();
    }
}
