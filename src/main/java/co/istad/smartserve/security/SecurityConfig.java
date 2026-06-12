package co.istad.smartserve.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                .requestMatchers("/api/v1/restaurants/**").permitAll()
                .anyRequest().authenticated()
        );
        return http.build();
    }
}
