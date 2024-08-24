package com.emazon.stock.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Marlon Rivera",
                        email = "riveramarlon33@gmail.com",
                        url = "https://marlonrivera.vercel.app/"
                ),
                description = "OpenApi documentation for microservice stock of Emazon",
                title = "OpenApi Microservice Stock ",
                version = "0.0.1"
        ),
        servers = @Server(
                description = "Local ENV",
                url = "http://localhost:8080"
        )
)
public class OpenApiConfig {
}