package ru.hits.musicservice.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.UUID;

/**
 * Класс, представляющий аутентификацию по JWT-токену.
 */
@Slf4j
public class JwtAuthentication extends AbstractAuthenticationToken {
    public JwtAuthentication(UUID id) {
        super(null);
        this.setDetails(id);
        setAuthenticated(true);
    }

    /**
     * Возвращает данные, необходимые для аутентификации. В данном случае - null.
     */
    @Override
    public Object getCredentials() {
        return null;
    }

    /**
     * Возвращает данные об аутентифицированном пользователе.
     *
     * @return данные об аутентифицированном пользователе.
     */
    @Override
    public Object getPrincipal() {
        return getDetails();
    }

}
