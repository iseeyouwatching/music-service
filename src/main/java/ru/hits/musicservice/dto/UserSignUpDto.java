package ru.hits.musicservice.dto;

import lombok.*;
import ru.hits.musicservice.enumeration.Gender;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserSignUpDto {

    @NotBlank(message = "Почта не может быть пустой.")
    @Email(message = "Некорректный формат почты.")
    private String email;

    @NotBlank(message = "Пароль не может быть пустым.")
    @Size(min = 8, max = 32, message = "Длина пароля должна быть от 8 до 32 символов.")
    private String password;

    @NotBlank(message = "Ник не может быть пустым.")
    @Size(min = 1, message = "Длина ника должна быть не менее 1 символа.")
    private String username;

    @Min(value = 1, message = "Некорректный возраст.")
    private int age;

    private Gender gender;

}
