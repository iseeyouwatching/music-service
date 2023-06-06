package ru.hits.musicservice.dto;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserUpdateInfoDto {

    private UUID avatar;

    private String username;

    private String name;

    private String surname;

    private String city;

    private String country;

    private String bio;

}
