package com.example.springapirest.sso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.WebFilterChainServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.util.matcher.MediaTypeServerWebExchangeMatcher;
import reactor.core.publisher.Mono;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
        http
                .csrf().disable()
                .authorizeExchange()
                .anyExchange().hasAuthority("ADMIN");
        return http.build();
    }
    public MapReactiveUserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("ADMIN")
                .password("ADMIN")
                .roles("ADMIN")
                .build();
        log.info(user.toString());
        return new MapReactiveUserDetailsService(user);
    }
}
