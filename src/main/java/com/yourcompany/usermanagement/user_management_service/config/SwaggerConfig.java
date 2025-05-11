package com.yourcompany.usermanagement.user_management_service.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "User Management API",
                version = "1.0",
                description = "REST API for managing users and their addresses. Includes user authentication, password updates, and integration with external services (e.g., ViaCEP).",
                contact = @Contact(
                        name = "Denilson Souza",
                        email = "denilson_contato@outlook.com",
                        url = "https://github.com/DhenSouza/user-management-service"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local Development Server")
        }
)
public class SwaggerConfig {
}
