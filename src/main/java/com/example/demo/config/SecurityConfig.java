// // package com.example.demo.config;

// // import com.example.demo.security.JwtAuthenticationFilter;
// // import org.springframework.context.annotation.Bean;
// // import org.springframework.context.annotation.Configuration;
// // import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// // import org.springframework.security.config.http.SessionCreationPolicy;
// // import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// // import org.springframework.security.crypto.password.PasswordEncoder;
// // import org.springframework.security.web.SecurityFilterChain;
// // import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// // @Configuration
// // public class SecurityConfig {

// //     @Bean
// //     public PasswordEncoder passwordEncoder() {
// //         return new BCryptPasswordEncoder();
// //     }

// //     @Bean
// //     public JwtAuthenticationFilter jwtAuthenticationFilter() {
// //         return new JwtAuthenticationFilter();
// //     }

// //     @Bean
// //     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

// //         http
// //             .csrf(csrf -> csrf.disable())
// //             .sessionManagement(session ->
// //                 session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
// //             )
// //             .authorizeHttpRequests(auth -> auth
// //                 .requestMatchers("/auth/**", "/swagger-ui/**", "/v3/api-docs/**")
// //                 .permitAll()
// //                 .anyRequest().authenticated()
// //             );

// //         http.addFilterBefore(
// //                 jwtAuthenticationFilter(),
// //                 UsernamePasswordAuthenticationFilter.class
// //         );

// //         return http.build();
// //     }
// // }
// package com.example.demo.config;

// import com.example.demo.security.JwtAuthenticationFilter;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// @Configuration
// public class SecurityConfig {

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }

//     @Bean
//     public JwtAuthenticationFilter jwtAuthenticationFilter() {
//         return new JwtAuthenticationFilter();
//     }

//     @Bean
//     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

//         http
//             .csrf(csrf -> csrf.disable())
//             .sessionManagement(session ->
//                 session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//             )
//             .authorizeHttpRequests(auth -> auth
//                 // Allow public access to auth endpoints
//                 .requestMatchers("/auth/**").permitAll()
//                 // Allow Swagger UI and API docs without auth
//                 .requestMatchers(
//                     "/swagger-ui/**",
//                     "/v3/api-docs/**",
//                     "/swagger-ui.html",
//                     "/webjars/**"
//                 ).permitAll()
//                 // All other endpoints require authentication
//                 .anyRequest().authenticated()
//             );

//         http.addFilterBefore(
//                 jwtAuthenticationFilter(),
//                 UsernamePasswordAuthenticationFilter.class
//         );

//         return http.build();
//     }
// }
package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // Disable CSRF
            .csrf(csrf -> csrf.disable())
            
            // Stateless session (not strictly needed if no JWT)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // Authorization rules
            .authorizeHttpRequests(auth -> auth
                // Auth endpoints
                .requestMatchers("/auth-controller/**").permitAll()
                
                // Swagger endpoints
                .requestMatchers(
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-ui.html",
                    "/webjars/**"
                ).permitAll()
                
                // All other endpoints are public
                .anyRequest().permitAll()
            );

        return http.build();
    }
}
