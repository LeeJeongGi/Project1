package com.project.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("project")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI whaiOpenAPI() {
        return new OpenAPI().info(info())
                .components(components());
    }

    private Info info() {
        return new Info()
                .title("Project API")
                .description("Project swagger config")
                .version("1.0")
                .license(new License().name("Apache 2.0").url("http://springdoc.org"));
    }

    private Components components() {
        return new Components()
                .addSecuritySchemes("bearer-key",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP)
                                .scheme("bearer").bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER).name("Authorization"));
    }
}
