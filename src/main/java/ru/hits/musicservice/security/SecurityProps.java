package ru.hits.musicservice.security;

import lombok.*;
    import org.springframework.boot.context.properties.ConfigurationProperties;
@ConfigurationProperties("jwt-token")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
public class SecurityProps {

    private String[] permitAll;

    private String secret;

    private String issuer;

    private String subject;

    private String rootPath;


}
