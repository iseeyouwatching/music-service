package ru.hits.musicservice.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserSignInDto {

    @Email(message = "Некорректный формат почты.")
    @NotBlank(message = "Почта не может быть пустой.")
    private String email;

    @NotBlank(message = "Пароль не может быть пустым.")
    private String password;

}
