package ru.hits.musicservice.dto;

import lombok.*;
import ru.hits.musicservice.entity.UserEntity;
import ru.hits.musicservice.enumeration.Gender;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserProfileDto {

    private UUID id;

    private String email;

    private String username;

    private String name;

    private String surname;

    private String city;

    private String country;

    private int age;

    private Gender gender;

    private String bio;

    private UUID avatar;

    public UserProfileDto(UserEntity user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.city = user.getCity();
        this.country = user.getCountry();
        this.age = user.getAge();
        this.gender = user.getGender();
        this.bio = user.getBio();
        this.avatar = user.getAvatar();
    }

}
