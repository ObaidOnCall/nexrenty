package ma.crm.carental.conf;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;
import ma.crm.carental.tenantfilter.TenantContext;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Slf4j
public class KeycloakRealmRolesGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>>{
    private String authorityPrefix = "";

    public KeycloakRealmRolesGrantedAuthoritiesConverter() {
    }

    public KeycloakRealmRolesGrantedAuthoritiesConverter setAuthorityPrefix(String authorityPrefix) {
        Assert.notNull(authorityPrefix, "authorityPrefix cannot be null");
        this.authorityPrefix = authorityPrefix;
        return this;
    }

    /**
     * Get authorities from the {@code realm_access.roles} jwt claim
     *
     * @param source the source object to convert, which must be an instance of {@link Jwt} (never {@code null})
     * @return collection of {@link GrantedAuthority}
     */
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

        /**
         * @just provide the currentTenantId here without create a spreate filter use (orgId)
        */

        TenantContext.setTenantId(orgId);
        log.warn("Tenant ID set: {} 🔖", TenantContext.getTenantId());

        
        /**
         * @
         * @
         */
        // Extract roles from the organization
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
