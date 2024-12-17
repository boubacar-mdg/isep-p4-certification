package com.isep.certification.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.isep.certification.config.filters.JwtAuthenticationFilter;
import com.isep.certification.users.models.enums.Role;

import static com.isep.certification.users.models.enums.Permission.ADMIN_CREATE;
import static com.isep.certification.users.models.enums.Permission.ADMIN_DELETE;
import static com.isep.certification.users.models.enums.Permission.ADMIN_READ;
import static com.isep.certification.users.models.enums.Permission.ADMIN_UPDATE;
import static com.isep.certification.users.models.enums.Permission.MANAGER_CREATE;
import static com.isep.certification.users.models.enums.Permission.MANAGER_DELETE;
import static com.isep.certification.users.models.enums.Permission.MANAGER_READ;
import static com.isep.certification.users.models.enums.Permission.MANAGER_UPDATE;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

        public static final String[] WHITE_LIST_URL = {
                        "/api/v1/auth/**",
                        "/api/v1/users/forgot/**",
                        "/api/v1/users/check/session",
                        "/api/v1/config/application",
                        "/api/v1/config/mock/upload",
                        "/api/v1/system/**",
                        "/api/v1/rentals/public/**",
                        "/api/v1/countries/**",
                        "/api/v1/cars/**",
                        "/v2/api-docs",
                        "/v3/api-docs",
                        "/v3/api-docs/**",
                        "/api/error",
                        "/swagger-resources",
                        "/swagger-resources/**",
                        "/configuration/ui",
                        "/configuration/security",
                        "/swagger-ui/**",
                        "/webjars/**",
                        "/swagger-ui.html"
        };
        // "/v2/api-docs",
        // "/v3/api-docs",
        // "/v3/api-docs/**",
        // "/swagger-resources",
        // "/swagger-resources/**",
        // "/configuration/ui",
        // "/configuration/security",
        // "/swagger-ui/**",
        // "/webjars/**",
        // "/swagger-ui.html"
        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;
        private final LogoutHandler logoutHandler;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(req -> req.requestMatchers(WHITE_LIST_URL)
                                                .permitAll()
                                                .requestMatchers("/api/v1/management/**")
                                                .hasAnyRole(Role.ADMIN.name(), Role.MANAGER.name())
                                                .requestMatchers("/api/v1/renter/**")
                                                .hasAnyRole(Role.ADMIN.name(), Role.RENTER.name())
                                                /*
                                                 * .requestMatchers(GET, "/api/v1/management/**")
                                                 * .hasAnyAuthority(ADMIN_READ.name(), MANAGER_READ.name())
                                                 * .requestMatchers(POST, "/api/v1/management/**")
                                                 * .hasAnyAuthority(ADMIN_CREATE.name(), MANAGER_CREATE.name())
                                                 * .requestMatchers(PUT, "/api/v1/management/**")
                                                 * .hasAnyAuthority(ADMIN_UPDATE.name(), MANAGER_UPDATE.name())
                                                 * .requestMatchers(DELETE, "/api/v1/management/**")
                                                 * .hasAnyAuthority(ADMIN_DELETE.name(), MANAGER_DELETE.name())
                                                 */
                                                .anyRequest()
                                                .authenticated())
                                .cors(cors -> {

                                        String corsAlowedOrigins = "http://localhost:4200,http://localhost:3000,http://localhost:5173,http://localhost:5174,http://localhost:4173,http://localhost:81";

                                        CorsConfiguration configuration = new CorsConfiguration();
                                        configuration.setAllowedOrigins(Arrays.asList(corsAlowedOrigins.split(",")));
                                        configuration.setAllowedMethods(Arrays.asList("*"));
                                        configuration.setAllowedHeaders(Arrays.asList("*"));
                                        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                                        configuration.setAllowCredentials(true);
                                        source.registerCorsConfiguration("/**", configuration);

                                        cors.configurationSource(source);
                                })
                                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                                .logout(logout -> logout.logoutUrl("/api/v1/auth/logout")
                                                .addLogoutHandler(logoutHandler)
                                                .logoutSuccessHandler((request, response,
                                                                authentication) -> SecurityContextHolder
                                                                                .clearContext()))
                                .exceptionHandling(exception -> exception
                                                .accessDeniedPage("/api/error"));

                return http.build();
        }
}
