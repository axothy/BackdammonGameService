package ru.axothy.backdammon.gameservice.config;

import lombok.Getter;
import lombok.Setter;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class KeycloakConfiguration {
    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.resource}")
    private String resource;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${username}")
    private String username;

    @Value("${password}")
    private String password;


    @Bean
    public KeycloakSpringBootConfigResolver KeycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    @Bean
    public Keycloak keycloak() {
        Keycloak keycloak = KeycloakBuilder
                .builder()
                .serverUrl(authServerUrl)
                .realm("master")
                .username(username)
                .password(password)
                .clientId("admin-cli")
                .build();

        return keycloak;
    }

    public String getRealm() {
        return realm;
    }
}

