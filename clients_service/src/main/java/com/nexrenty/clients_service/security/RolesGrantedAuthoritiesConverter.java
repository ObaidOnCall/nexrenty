package com.nexrenty.clients_service.security;


import java.util.Objects;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.Assert;

import com.nexrenty.clients_service.utils.TenantContext;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;



@NoArgsConstructor
@Slf4j
public class RolesGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>>{
    private String authorityPrefix = "";


    public RolesGrantedAuthoritiesConverter setAuthorityPrefix(String authorityPrefix) {
        Assert.notNull(authorityPrefix, "authorityPrefix cannot be null");
        this.authorityPrefix = authorityPrefix;
        return this;
    }

    
    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        
        Map<String, Object> organizations = source.getClaim("organizations");
        if (Objects.isNull(organizations) || organizations.isEmpty()) {
            return Collections.emptyList();
        }

        Set<GrantedAuthority> authorities = new HashSet<>();

        // Get the first organization


        Map.Entry<String, Object> firstOrgEntry = organizations.entrySet().iterator().next();
        String orgId = firstOrgEntry.getKey();
        
        @SuppressWarnings("unchecked")
        Map<String, Object> organizationDetails = (Map<String, Object>) firstOrgEntry.getValue();


        TenantContext.setTenantId(orgId);

        Object roles = organizationDetails.get("roles");
        if (Objects.isNull(roles) || !(roles instanceof Collection<?>)) {
            return Collections.emptyList();
        }

        Collection<?> rolesCollection = (Collection<?>) roles;
        
        // Convert roles to GrantedAuthority
        rolesCollection.stream()
            .filter(String.class::isInstance) // Ensure roles are strings
            .map(role -> new SimpleGrantedAuthority(authorityPrefix  + (String) role))
            .forEach(authorities::add);

        return authorities;

    }

}
