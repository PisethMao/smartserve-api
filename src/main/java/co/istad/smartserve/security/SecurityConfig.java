package co.istad.smartserve.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
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
        );
        return http.build();
    }
}
