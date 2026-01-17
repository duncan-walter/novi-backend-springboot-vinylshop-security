package walter.duncan.vinylwebshop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.jwt.JwtAudienceValidator;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
/* TODO: Find a way to make integration tests work with security without editing main production code
    Adding addFilters = false to the test class was not enough. This configuration class was still being executed
    resulting in failing tests since the configure(HttpSecurity http) method is trying to contact the (not running) keycloak server.
 */
@Profile("!test")
public class SecurityConfig {
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer;

    @Value("${spring.security.oauth2.resourceserver.jwt.audiences}")
    private String audience;

    @Value("${client-id}")
    private String clientId;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> { })
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                                .decoder(jwtDecoder())
                        ))
                .authorizeHttpRequests(authorize -> authorize
                        // ADMIN routes
                        .requestMatchers("/albums/{id}/stocks/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,
                                "/albums", "albums/{id}/**",
                                "/artists",
                                "/genres",
                                "/publishers"
                        ).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,
                                "/albums/{id}",
                                "/artists/{id}",
                                "/genres/{id}",
                                "/publishers/{id}"
                        ).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,
                                "/albums/{id}/**",
                                "/artists/{id}",
                                "/genres/{id}",
                                "/publishers/{id}"
                        ).hasRole("ADMIN")

                        // Authenticated routes
                        .requestMatchers(
                                "/artists/**",
                                "/genres/**",
                                "/publishers/**"
                        ).authenticated()

                        // Public routes
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/albums/**").permitAll()

                        // Default
                        .anyRequest().denyAll()
                )
                .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    public JwtDecoder jwtDecoder(){
        NimbusJwtDecoder jwtDecoder = JwtDecoders.fromOidcIssuerLocation(issuer);
        OAuth2TokenValidator<Jwt> audienceValidator = new JwtAudienceValidator(audience);
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);
        jwtDecoder.setJwtValidator(withAudience);

        return jwtDecoder;
    }

    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new Converter<>() {
            @Override
            public Collection<GrantedAuthority> convert(Jwt source) {
                Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

                for (String authority : getAuthorities(source)) {
                    grantedAuthorities.add(new SimpleGrantedAuthority(authority));
                }

                return grantedAuthorities;
            }

            private List<String> getAuthorities(Jwt jwt){
                Map<String, Object> resourceAccess = jwt.getClaim("resource_access");

                if (resourceAccess != null) {
                    if (resourceAccess.get(clientId) instanceof Map) {
                        Map<String, Object> client = (Map<String, Object>) resourceAccess.get(clientId);

                        if (client != null && client.containsKey("roles")) {
                            return (List<String>) client.get("roles");
                        }
                    } else {
                        Map<String, Object> realmAcces = jwt.getClaim("realm_access");

                        if (realmAcces != null && realmAcces.containsKey("roles")) {
                            return (List<String>) realmAcces.get("roles");
                        }

                        return new ArrayList<>();
                    }
                }

                return new ArrayList<>();
            }
        });

        return jwtAuthenticationConverter;
    }
}