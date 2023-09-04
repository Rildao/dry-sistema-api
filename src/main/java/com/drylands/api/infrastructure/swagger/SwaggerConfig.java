package com.drylands.api.infrastructure.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(info = @Info(
        contact = @Contact(name = "Dry Land's Modas Sistema"),
        title = "Dry Land's Modas - API",
        version = "0.0.1"),
        security = @SecurityRequirement(name = "JWT Token"))
@SecurityScheme(
        name = "JWT Token",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@Configuration
public class SwaggerConfig {
}
