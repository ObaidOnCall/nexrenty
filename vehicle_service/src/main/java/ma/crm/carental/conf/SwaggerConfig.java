package ma.crm.carental.conf;

import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

        @Value("${swagger.server-urls}")
        private List<String> serverUrls;

        @SuppressWarnings("unchecked")
        @Bean
        public OpenAPI customOpenAPI() {
        
        // Map the URLs to Server objects for Swagger
        List<Server> servers = serverUrls.stream()
                .map(url -> new Server().url(url))
                .collect(Collectors.toList());
        
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .info(new Info().title("Car Renatl Service")
                        .description("This is auth service use for validate the user.")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("SpringBoot Wiki Documentation")
                        .url("https://springboot.wiki.github.org/docs"))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                                .addRequestBodies("FileUploadRequest",
                                new RequestBody()
                                .content(new Content()
                                        .addMediaType("multipart/form-data",
                                        new MediaType()
                                                .schema(new Schema<>()
                                                .type("array")
                                                .items(new Schema<>()
                                                        .type("string")
                                                        .format("binary"))))))
                )
                .servers(servers);
        }
}