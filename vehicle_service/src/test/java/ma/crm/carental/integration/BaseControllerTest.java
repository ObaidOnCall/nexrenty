package ma.crm.carental.integration;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import lombok.extern.slf4j.Slf4j;
import ma.crm.carental.tenantfilter.TenantContext;

@SpringBootTest
@ContextConfiguration // Cache the application context
@Slf4j
public abstract class BaseControllerTest {

    BaseControllerTest() {

        String tenantId = "test-tenant-id";  // Mock tenant ID

        /**
         * @ use test tenant Id and pass it to the tenantcontext and set it onec
         */

        TenantContext.setTenantId(tenantId);

    }

    protected static String accessToken;


    protected MockHttpServletRequestBuilder prepareRequestWithOAuthToken(HttpMethod httpMethod, String url) {


        MockHttpServletRequestBuilder requestBuilder;
        switch (httpMethod.name()) {
            case "POST":
                requestBuilder = MockMvcRequestBuilders.post(url);
                break;
            case "PUT":
                requestBuilder = MockMvcRequestBuilders.put(url);
                break;
            case "GET":
                requestBuilder = MockMvcRequestBuilders.get(url);
                break;
            case "DELETE":
                requestBuilder = MockMvcRequestBuilders.delete(url);
                break;
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + httpMethod);
        }


        return requestBuilder
            .with(SecurityMockMvcRequestPostProcessors.jwt()  // Using mock JWT token
                .jwt(jwt -> jwt.claim("scope", "write")
                            .claim("preferred_username", "testuser")
                            .claim("organizations", Map.of(
                                "26544c7a-ecc3-422d-8abf-f0619a048cd0", Map.of(
                                    "roles", List.of("view-organization", "manage-organization"),
                                    "name", "first-org"
                                )
                            ))
                )
            )
            .contentType(MediaType.APPLICATION_JSON);

    }
}
