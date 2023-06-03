package ru.hits.musicservice.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.hits.musicservice.security.SecurityProps;

import java.util.Objects;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final JWTFilter jwtFilter;

    private final SecurityProps securityProps;

    @SneakyThrows
    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) {
        http = http.requestMatcher(request -> Objects.nonNull(request.getServletPath())
                        && request.getServletPath().startsWith(securityProps.getRootPath()))
                .authorizeRequests()
                .antMatchers(securityProps.getPermitAll()).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return finalize(http);
    }

    @SneakyThrows
    @Bean
    public SecurityFilterChain filterChainDenyAll(HttpSecurity http) {
        http = http.requestMatcher(request -> Objects.nonNull(request.getServletPath())
                        && !request.getServletPath().startsWith(securityProps.getRootPath()))
                .authorizeRequests()
                .anyRequest().permitAll()
                .and();
        return finalize(http);
    }

    @SneakyThrows
    private SecurityFilterChain finalize(HttpSecurity http) {
        return http.csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .cors()
                .and()
                .build();
    }

}
