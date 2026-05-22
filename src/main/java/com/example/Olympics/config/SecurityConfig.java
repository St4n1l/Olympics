package com.example.Olympics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // public endpoints
                        .requestMatchers("/api/register/view").permitAll()
                        .requestMatchers("/api/register").permitAll()
                        .requestMatchers("/api/olympics/*/medals").permitAll()
                        .requestMatchers("/api/olympics/*/average-age").permitAll()
                        .requestMatchers("/api/olympics/*/medalist-ages").permitAll()
                        .requestMatchers("/api/slalom/*/ranking").permitAll()
                        .requestMatchers("/api/biathlon/*/ranking").permitAll()
                        .requestMatchers("/view/**").permitAll()
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/api/olympics/view").permitAll()
                        .requestMatchers("/api/athletes/view").permitAll()
                        .requestMatchers("/logout").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/competitions").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/athletes").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/slalom/*/qualifiers").permitAll()

                        // athlete endpoints
                        .requestMatchers(HttpMethod.POST, "/api/athletes").hasRole("ATHLETE")
                        .requestMatchers(HttpMethod.PUT, "/api/athletes/**").hasRole("ATHLETE")
                        .requestMatchers(HttpMethod.DELETE, "/api/athletes/**").hasRole("ATHLETE")
                        // admin endpoints
                        .requestMatchers(HttpMethod.POST, "/api/competitions/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/slalom/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/biathlon/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/olympics").hasRole("ADMIN")
                        // everything else needs login
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/oauth2/authorization/keycloak")
                        .defaultSuccessUrl("/", true)
                        .userInfoEndpoint(userInfo -> userInfo.oidcUserService(oidcUserService()))
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            request.getSession().invalidate();
                            response.sendRedirect("http://localhost:8180/realms/Olympics/protocol/openid-connect/logout"
                                    + "?post_logout_redirect_uri=http://localhost:8080"
                                    + "&client_id=olympics-app");
                        })
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthoritiesClaimName("realm_access.roles");
        converter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
            if (realmAccess == null || !realmAccess.containsKey("roles")) return List.of();
            List<String> roles = (List<String>) realmAccess.get("roles");
            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());
        });
        return jwtConverter;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        OidcUserService delegate = new OidcUserService();
        return userRequest -> {
            OidcUser oidcUser = delegate.loadUser(userRequest);

            // Extract roles from realm_access.roles
            Map<String, Object> claims = oidcUser.getClaims();
            Map<String, Object> realmAccess = (Map<String, Object>) claims.get("realm_access");

            Set<GrantedAuthority> authorities = new HashSet<>(oidcUser.getAuthorities());

            if (realmAccess != null && realmAccess.containsKey("roles")) {
                List<String> roles = (List<String>) realmAccess.get("roles");
                roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .forEach(authorities::add);
            }

            return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
        };
    }
}