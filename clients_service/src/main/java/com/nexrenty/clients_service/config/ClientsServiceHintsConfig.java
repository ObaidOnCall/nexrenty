package com.nexrenty.clients_service.config;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public class ClientsServiceHintsConfig implements RuntimeHintsRegistrar{

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {

        // hints.reflection().registerType(Schema.class , MemberCategory.values()) ;

        // hints.reflection().registerType(Parameter.class , MemberCategory.values()) ;

        // hints.reflection().registerType(ApiResponses.class , MemberCategory.values()) ;

        // hints.reflection().registerType(ApiResponse.class , MemberCategory.values()) ;
    }
    
}
