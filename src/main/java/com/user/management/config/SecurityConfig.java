package com.user.management.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String secret;

    // you’ll probably inject JwtUtil + UserService here later
    // private final JwtUtil jwtUtil;
    // private final UserService userService;

    // public SecurityConfig(JwtUtil jwtUtil, UserService userService) {
    //     this.jwtUtil = jwtUtil;
    //     this.userService = userService;
    // }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/users/login",
                                "/api/users/register",
                                "/api/users/companies",
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/swagger-resources/**",
                                "/swagger/**",
                                "/webjars/**",
                                "/actuator/**"
                        ).permitAll()
                        // OAuth2-related endpoints should be public
                        .requestMatchers(
                                "/oauth2/**",
                                "/login/oauth2/**"
                        ).permitAll()
                        // admin-only APIs
                        .requestMatchers("/admin/**").hasAuthority("SUPER_ADMIN")

                        // everything else needs authentication
                        .anyRequest().authenticated()
                )
                // still stateless – JWT is used after login / OAuth2
                .sessionManagement(sess ->
                        sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

//                // 1) enable OAuth2 login (e.g. Google / GitHub / etc.)
//                .oauth2Login(oauth2 -> oauth2
//                                // after successful OAuth2 login, we will generate our JWT
//                                .successHandler(oauth2AuthenticationSuccessHandler())
//                        // you can also customize failure handler or login page if needed
//                        // .failureHandler(...)
//                )

                // 2) keep your existing custom JWT filter for “normal” API calls
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // existing JWT filter
    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(secret);
    }


    @Bean
    public AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request,
                                                HttpServletResponse response,
                                                Authentication authentication)
                    throws IOException, ServletException {

                // 1) Get the OAuth2 user (email, name, etc.)
                //    Example:
                //    OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
                //    String email = (String) oAuth2User.getAttributes().get("email");

                // 2) Find or create a User in your DB based on that email
                //    User user = userService.findOrCreateFromOAuth2(oAuth2User);

                // 3) Generate JWT for that user using your existing JwtUtil
                //    String token = jwtUtil.generateToken(user);

                // 4) Return the token to frontend (for SPA/React)
                // For now, just a placeholder JSON – you will replace this with real logic
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                // TODO: replace "dummy-token" with actual generated JWT
                String body = """
                        {
                          "message": "OAuth2 login success",
                          "token": "dummy-token"
                        }
                        """;

                response.getWriter().write(body);
                response.flushBuffer();
            }
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
