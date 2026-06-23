package co.istad.smartserve.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

/**
 * Security configuration for SmartServe API.
 * Handles JWT authentication, authorization, and access control.
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthoritiesClaimName("authorities");
        authoritiesConverter.setAuthorityPrefix("ROLE_");
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(endpoints -> endpoints
                .requestMatchers(
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs",
                        "/v3/api-docs/**",
                        "/v3/api-docs/swagger-config",
                        "/scalar",
                        "/scalar/**"
                ).permitAll()
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/v1/restaurants",
                        "/api/v1/restaurants/*"
                ).permitAll()
                .requestMatchers(
                        "/api/v1/restaurants/*/categories",
                        "/api/v1/categories/*"
                ).permitAll()
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/v1/restaurants/*/menu-items",
                        "/api/v1/restaurants/*/public-menu/**",
                        "/api/v1/menu-items/*"
                ).permitAll()
                .requestMatchers(
                        HttpMethod.POST,
                        "/api/v1/restaurants/*/menu-items"
                ).permitAll()
                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/v1/menu-items/**"
                ).hasAnyRole("ADMIN", "OWNER", "MANAGER")
                .requestMatchers(
                        HttpMethod.PATCH,
                        "/api/v1/menu-items/**"
                ).permitAll()
                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/v1/menu-items/**"
                ).permitAll()
                .anyRequest().authenticated()
        )
        .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
        )
        .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint((request, response, ex) -> {
                    response.sendError(401, "Unauthorized");
                })
                .accessDeniedHandler((request, response, ex) -> {
                    response.sendError(403, "Forbidden");
                })
        );
        return http.build();
    }
}
