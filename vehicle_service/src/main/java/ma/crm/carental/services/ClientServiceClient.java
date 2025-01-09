package ma.crm.carental.services;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import ma.crm.carental.conf.JwtTokenPropagator;
import ma.crm.carental.dtos.client.ClientResponseDto;

@FeignClient(name = "clients-service" , configuration = JwtTokenPropagator.class)
public interface ClientServiceClient {

    Logger logger = LoggerFactory.getLogger(ClientServiceClient.class);

    @GetMapping("/clients/{ids}")
    @CircuitBreaker(name = "clientServiceBreaker", fallbackMethod = "findClientsFallback")
    List<ClientResponseDto> findClients(@PathVariable List<Long> ids) ;





    default List<ClientResponseDto> findClientsFallback(List<Long> ids, Throwable throwable) {
        
        logger.error("Fallback triggered for clients: {}", ids, throwable);
        return Collections.emptyList();
    }
}
