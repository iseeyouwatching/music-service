package ru.hits.musicservice.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.hits.musicservice.dto.ApiError;
import ru.hits.musicservice.exception.TokenNotValidException;
import ru.hits.musicservice.security.JWTUtil;
import ru.hits.musicservice.security.JwtAuthentication;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter  {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = httpServletRequest.getHeader("Authorization");

        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);

            if (jwt.isBlank()) {
                httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Невалидный токен. JWT-токен пустой.");
            } else {
                try {
                    UUID id = jwtUtil.validateTokenAndRetrieveClaim(jwt);
                    var authentication = new JwtAuthentication(id);

                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                 } catch (TokenNotValidException exc) {
                    sendError(httpServletResponse, 450, exc.getMessage());
                } catch (JWTVerificationException exc) {
                    httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,
                            "Невалидный токен. JWT-токен не прошел проверку.");
                }
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void sendError(HttpServletResponse response,
                           Integer statusCode,
                           String message
    ) throws IOException {
        ApiError error = new ApiError(message);
        String responseBody = new Gson().toJson(error);

        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.print(responseBody);
        out.flush();
    }

}
