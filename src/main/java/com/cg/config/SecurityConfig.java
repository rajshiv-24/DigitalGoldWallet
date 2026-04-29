package com.cg.config;

import com.cg.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()

                        // Public because user registration currently needs an addressId first.
                        .requestMatchers(HttpMethod.POST, "/api/addresses").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()

                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // Users: users can access only their own record via method-level checks.
                        .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users/count").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users/by-email").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users/*/exists").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users/*").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/users/*").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/users/*/balance").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/users/wallet/top-up").authenticated()

                        // Gold operations: users can act on and view only their own userId.
                        .requestMatchers(HttpMethod.POST, "/api/gold/buy").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/gold/sell").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/gold/convert-to-physical").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/gold/holdings/by-user/*").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/gold/holdings/by-user/*/total").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/gold/history/by-user/*").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/gold/physical/by-user/*").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/gold/summary/by-user/*").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/gold/**").hasRole("ADMIN")

                        // Payments: users can view only their own payments; aggregate/payment admin views are admin-only.
                        .requestMatchers(HttpMethod.GET, "/api/payments/by-user/*").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/payments/by-user/*/total").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/payments/**").hasRole("ADMIN")

                        // Vendors and branches: users can browse and check prices/stock; admin manages records.
                        .requestMatchers(HttpMethod.GET, "/api/vendors/count").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/vendors/by-email").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/vendors/*/exists").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/vendors/by-name").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/vendors").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/vendors/*").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/vendors/*/price").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/vendors/*/branches").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/vendors/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/vendors").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/vendors/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/vendors/*").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/branches/count").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/branches/*/exists").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/branches/by-vendor/*").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/branches/by-city").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/branches/with-min-quantity").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/branches").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/branches/*").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/branches/*/stock").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/branches/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/branches").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/branches/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/branches/*").hasRole("ADMIN")

                        // Addresses contain personal location details, so reads/searches/changes are admin-only.
                        .requestMatchers(HttpMethod.GET, "/api/addresses/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/addresses/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/addresses/*").hasRole("ADMIN")

                        // Metadata enum values are safe for forms; system counts are admin-only.
                        .requestMatchers(HttpMethod.GET, "/api/metadata/endpoints/count").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/metadata/**").authenticated()

                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .authenticationProvider(authenticationProvider())
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
