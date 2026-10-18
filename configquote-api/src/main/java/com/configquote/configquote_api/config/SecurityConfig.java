//
//package com.configquote.configquote_api.config;
//
//import java.util.Base64;
//
//import javax.crypto.SecretKey;
//import javax.crypto.spec.SecretKeySpec;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.ProviderManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.security.oauth2.jwt.JwtEncoder;
//import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
//import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
//import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//	@Bean
//	PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//
//	@Bean
//	AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
//			PasswordEncoder passwordEncoder) {
//
//		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
//
//		provider.setPasswordEncoder(passwordEncoder);
//
//		return new ProviderManager(provider);
//	}
//
//	@Bean
//	SecretKey jwtSecretKey(@Value("${app.jwt.secret}") String base64Secret) {
//
//		byte[] bytes = Base64.getDecoder().decode(base64Secret);
//
//		if (bytes.length < 32) {
//			throw new IllegalArgumentException("JWT secret must contain at least 32 bytes");
//		}
//
//		return new SecretKeySpec(bytes, "HmacSHA256");
//	}
//
//	@Bean
//	JwtEncoder jwtEncoder(SecretKey jwtSecretKey) {
//		return NimbusJwtEncoder.withSecretKey(jwtSecretKey).algorithm(MacAlgorithm.HS256).build();
//	}
//
//	@Bean
//	JwtDecoder jwtDecoder(SecretKey jwtSecretKey) {
//		return NimbusJwtDecoder.withSecretKey(jwtSecretKey).macAlgorithm(MacAlgorithm.HS256).build();
//	}
//
//	@Bean
//	JwtAuthenticationConverter jwtAuthenticationConverter() {
//
//		JwtGrantedAuthoritiesConverter rolesConverter = new JwtGrantedAuthoritiesConverter();
//
//		rolesConverter.setAuthoritiesClaimName("roles");
//		rolesConverter.setAuthorityPrefix("ROLE_");
//
//		JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
//
//		converter.setJwtGrantedAuthoritiesConverter(rolesConverter);
//
//		return converter;
//	}
//
//	@Bean
//	SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationConverter jwtConverter)
//			throws Exception {
//
//		http.csrf(csrf -> csrf.disable())
//				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//				.httpBasic(basic -> basic.disable()).formLogin(form -> form.disable())
//				.authorizeHttpRequests(auth -> auth
//
//						// Public authentication and Swagger
//						.requestMatchers("/api/auth/login").permitAll()
//						.requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
//
//						// User management
//						.requestMatchers(HttpMethod.POST, "/api/auth/users").hasRole("ADMIN")
//
//						// Product management
//						.requestMatchers(HttpMethod.GET, "/api/products/**").authenticated()
//						.requestMatchers(HttpMethod.POST, "/api/products/**").hasRole("ADMIN")
//						.requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole("ADMIN")
//						.requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")
//
//						// Customers
//						.requestMatchers(HttpMethod.GET, "/api/customers/**").hasAnyRole("ADMIN", "SALES", "VIEWER")
//						.requestMatchers("/api/customers/**").hasAnyRole("ADMIN", "SALES")
//
//						// Quotations
//						.requestMatchers(HttpMethod.GET, "/api/quotations/**").hasAnyRole("ADMIN", "SALES", "VIEWER")
//						.requestMatchers("/api/quotations/**").hasAnyRole("ADMIN", "SALES")
//
//						// Everything else requires authentication
//						.anyRequest().authenticated())
//				.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtConverter)));
//
//		return http.build();
//	}
//}



//package com.configquote.configquote_api.config;
//
//import java.util.Base64;
//
//import javax.crypto.SecretKey;
//import javax.crypto.spec.SecretKeySpec;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.ProviderManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.security.oauth2.jwt.JwtEncoder;
//import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
//import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
//import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
//import org.springframework.security.web.SecurityFilterChain;
//
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//@Configuration
//public class SecurityConfig {
//
//    // ========================================
//    // 1. Password Encoder
//    // ========================================
//
//    @Bean
//    PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    // ========================================
//    // 2. Authentication Manager
//    // ========================================
//
//    @Bean
//    AuthenticationManager authenticationManager(
//            UserDetailsService userDetailsService,
//            PasswordEncoder passwordEncoder) {
//
//        DaoAuthenticationProvider provider =
//                new DaoAuthenticationProvider(userDetailsService);
//
//        provider.setPasswordEncoder(passwordEncoder);
//
//        return new ProviderManager(provider);
//    }
//
//    // ========================================
//    // 3. JWT Secret Key
//    // ========================================
//
//    @Bean
//    SecretKey jwtSecretKey(
//            @Value("${app.jwt.secret}") String base64Secret) {
//
//        byte[] bytes = Base64.getDecoder().decode(base64Secret);
//
//        if (bytes.length < 32) {
//            throw new IllegalArgumentException(
//                "JWT secret must contain at least 32 bytes");
//        }
//
//        return new SecretKeySpec(bytes, "HmacSHA256");
//    }
//
//    // ========================================
//    // 4. JWT Encoder
//    // ========================================
//
//    @Bean
//    JwtEncoder jwtEncoder(SecretKey jwtSecretKey) {
//
//        return NimbusJwtEncoder.withSecretKey(jwtSecretKey)
//                .algorithm(MacAlgorithm.HS256)
//                .build();
//    }
//
//    // ========================================
//    // 5. JWT Decoder
//    // ========================================
//
//    @Bean
//    JwtDecoder jwtDecoder(SecretKey jwtSecretKey) {
//
//        return NimbusJwtDecoder.withSecretKey(jwtSecretKey)
//                .macAlgorithm(MacAlgorithm.HS256)
//                .build();
//    }
//
//    // ========================================
//    // 6. JWT Roles Converter
//    // ========================================
//
//    @Bean
//    JwtAuthenticationConverter jwtAuthenticationConverter() {
//
//        JwtGrantedAuthoritiesConverter rolesConverter =
//                new JwtGrantedAuthoritiesConverter();
//
//        rolesConverter.setAuthoritiesClaimName("roles");
//        rolesConverter.setAuthorityPrefix("ROLE_");
//
//        JwtAuthenticationConverter converter =
//                new JwtAuthenticationConverter();
//
//        converter.setJwtGrantedAuthoritiesConverter(rolesConverter);
//
//        return converter;
//    }
//
//    // ========================================
//    // 7. Security Filter Chain
//    // ========================================
//
//    @Bean
//    SecurityFilterChain securityFilterChain(
//            HttpSecurity http,
//            JwtAuthenticationConverter jwtConverter) throws Exception {
//
//        http
//            // Stateless REST API using Bearer tokens
//            .csrf(csrf -> csrf.disable())
//
//            .sessionManagement(session ->
//                session.sessionCreationPolicy(
//                    SessionCreationPolicy.STATELESS))
//
//            .httpBasic(basic -> basic.disable())
//            .formLogin(form -> form.disable())
//
//            .authorizeHttpRequests(auth -> auth
//
//                // ========================================
//                // PUBLIC ENDPOINTS
//                // ========================================
//
//                .requestMatchers(
//                    HttpMethod.POST,
//                    "/api/auth/login"
//                ).permitAll()
//
//                .requestMatchers(
//                    "/swagger-ui/**",
//                    "/swagger-ui.html",
//                    "/v3/api-docs/**"
//                ).permitAll()
//
//                // ========================================
//                // USER MANAGEMENT
//                // ========================================
//
//                // Only ADMIN can create users
//                .requestMatchers(
//                    HttpMethod.POST,
//                    "/api/auth/users"
//                ).hasRole("ADMIN")
//
//                // ========================================
//                // PRODUCT CONFIGURATION & PRICING
//                // IMPORTANT: Before general product rules
//                // ========================================
//
//                // ADMIN, SALES, VIEWER can view options
//                .requestMatchers(
//                    HttpMethod.GET,
//                    "/api/products/*/options"
//                ).hasAnyRole("ADMIN", "SALES", "VIEWER")
//
//                // Only ADMIN and SALES can calculate prices
//                .requestMatchers(
//                    HttpMethod.POST,
//                    "/api/products/*/calculate-price"
//                ).hasAnyRole("ADMIN", "SALES")
//
//                // ========================================
//                // PRODUCT MANAGEMENT
//                // ========================================
//
//                // All authenticated users can view products
//                .requestMatchers(
//                    HttpMethod.GET,
//                    "/api/products/**"
//                ).hasAnyRole("ADMIN", "SALES", "VIEWER")
//
//                // Only ADMIN can create products
//                .requestMatchers(
//                    HttpMethod.POST,
//                    "/api/products/**"
//                ).hasRole("ADMIN")
//
//                // Only ADMIN can update products
//                .requestMatchers(
//                    HttpMethod.PUT,
//                    "/api/products/**"
//                ).hasRole("ADMIN")
//
//                // Only ADMIN can delete products
//                .requestMatchers(
//                    HttpMethod.DELETE,
//                    "/api/products/**"
//                ).hasRole("ADMIN")
//
//                // ========================================
//                // CUSTOMER MANAGEMENT
//                // ========================================
//
//                // ADMIN, SALES, VIEWER can view customers
//                .requestMatchers(
//                    HttpMethod.GET,
//                    "/api/customers/**"
//                ).hasAnyRole("ADMIN", "SALES", "VIEWER")
//
//                // ADMIN and SALES can modify customers
//                .requestMatchers(
//                    "/api/customers/**"
//                ).hasAnyRole("ADMIN", "SALES")
//
//                // ========================================
//                // QUOTATION MANAGEMENT
//                // ========================================
//
//                // ADMIN, SALES, VIEWER can view quotations
//                .requestMatchers(
//                    HttpMethod.GET,
//                    "/api/quotations/**"
//                ).hasAnyRole("ADMIN", "SALES", "VIEWER")
//
//                // ADMIN and SALES can create quotations
//                // and update quotation statuses
//                .requestMatchers(
//                    "/api/quotations/**"
//                ).hasAnyRole("ADMIN", "SALES")
//
//                // ========================================
//                // OTHER ENDPOINTS
//                // ========================================
//
//                .anyRequest().authenticated()
//            )
//
//            // ========================================
//            // JWT AUTHENTICATION
//            // ========================================
//
//            .oauth2ResourceServer(oauth2 ->
//                oauth2.jwt(jwt ->
//                    jwt.jwtAuthenticationConverter(jwtConverter)));
//
//        return http.build();
//    }
//}



package com.configquote.configquote_api.config;

import java.util.Base64;
import java.util.List;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    // ========================================
    // 1. PASSWORD ENCODER
    // ========================================

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ========================================
    // 2. AUTHENTICATION MANAGER
    // ========================================

    @Bean
    AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(provider);
    }

    // ========================================
    // 3. JWT SECRET KEY
    // ========================================

    @Bean
    SecretKey jwtSecretKey(
            @Value("${app.jwt.secret}") String base64Secret) {

        byte[] bytes = Base64.getDecoder().decode(base64Secret);

        if (bytes.length < 32) {
            throw new IllegalArgumentException(
                "JWT secret must contain at least 32 bytes"
            );
        }

        return new SecretKeySpec(bytes, "HmacSHA256");
    }

    // ========================================
    // 4. JWT ENCODER
    // ========================================

    @Bean
    JwtEncoder jwtEncoder(SecretKey jwtSecretKey) {

        return NimbusJwtEncoder.withSecretKey(jwtSecretKey)
                .algorithm(MacAlgorithm.HS256)
                .build();
    }

    // ========================================
    // 5. JWT DECODER
    // ========================================

    @Bean
    JwtDecoder jwtDecoder(SecretKey jwtSecretKey) {

        return NimbusJwtDecoder.withSecretKey(jwtSecretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    // ========================================
    // 6. JWT ROLES CONVERTER
    // ========================================

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {

        JwtGrantedAuthoritiesConverter rolesConverter =
                new JwtGrantedAuthoritiesConverter();

        rolesConverter.setAuthoritiesClaimName("roles");
        rolesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter converter =
                new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(rolesConverter);

        return converter;
    }

    // ========================================
    // 7. SECURITY FILTER CHAIN
    // ========================================

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationConverter jwtConverter) throws Exception {

        http

            // ========================================
            // CORS - ALLOW REACT FRONTEND
            // ========================================

            .cors(cors -> {})

            // ========================================
            // STATELESS JWT SECURITY
            // ========================================

            .csrf(csrf -> csrf.disable())

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .httpBasic(basic -> basic.disable())
            .formLogin(form -> form.disable())

            // ========================================
            // API AUTHORIZATION RULES
            // ========================================

            .authorizeHttpRequests(auth -> auth

                // ========================================
                // PUBLIC ENDPOINTS
                // ========================================

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/auth/login"
                ).permitAll()

                .requestMatchers(
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**"
                ).permitAll()

                // ========================================
                // USER MANAGEMENT
                // ========================================

                // Only ADMIN can create users
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/auth/users"
                ).hasRole("ADMIN")

                // ========================================
                // PRODUCT CONFIGURATION & PRICING
                // ========================================

                // ADMIN, SALES, VIEWER can view options
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/products/*/options"
                ).hasAnyRole("ADMIN", "SALES", "VIEWER")

                // ADMIN and SALES can calculate prices
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/products/*/calculate-price"
                ).hasAnyRole("ADMIN", "SALES")

                // ========================================
                // PRODUCT MANAGEMENT
                // ========================================

                // All three roles can view products
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/products/**"
                ).hasAnyRole("ADMIN", "SALES", "VIEWER")

                // Only ADMIN can create products
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/products/**"
                ).hasRole("ADMIN")

                // Only ADMIN can update products
                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/products/**"
                ).hasRole("ADMIN")

                // Only ADMIN can delete products
                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/products/**"
                ).hasRole("ADMIN")

                // ========================================
                // CUSTOMER MANAGEMENT
                // ========================================

                // All three roles can view customers
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/customers/**"
                ).hasAnyRole("ADMIN", "SALES", "VIEWER")

                // ADMIN and SALES can modify customers
                .requestMatchers(
                    "/api/customers/**"
                ).hasAnyRole("ADMIN", "SALES")

                // ========================================
                // QUOTATION MANAGEMENT
                // ========================================

                // All three roles can view quotations
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/quotations/**"
                ).hasAnyRole("ADMIN", "SALES", "VIEWER")

                // ADMIN and SALES can create quotations
                // and update quotation statuses
                .requestMatchers(
                    "/api/quotations/**"
                ).hasAnyRole("ADMIN", "SALES")

                // ========================================
                // OTHER ENDPOINTS
                // ========================================

                .anyRequest().authenticated()
            )

            // ========================================
            // JWT AUTHENTICATION
            // ========================================

            .oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwt ->
                    jwt.jwtAuthenticationConverter(jwtConverter)
                )
            );

        return http.build();
    }

    // ========================================
    // 8. CORS CONFIGURATION - REACT FRONTEND
    // ========================================

    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        // React Vite development server
        configuration.setAllowedOrigins(
            List.of("http://localhost:5173")
        );

        // HTTP methods used by ConfigQuote
        configuration.setAllowedMethods(
            List.of(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
            )
        );

        // Allow JWT Authorization header
        configuration.setAllowedHeaders(
            List.of(
                "Authorization",
                "Content-Type"
            )
        );

        // JWT is sent through Authorization header,
        // not browser cookies.
        configuration.setAllowCredentials(false);

        // Cache CORS preflight responses for 1 hour
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        // Apply CORS configuration to all API paths
        source.registerCorsConfiguration(
            "/**",
            configuration
        );

        return source;
    }
}

