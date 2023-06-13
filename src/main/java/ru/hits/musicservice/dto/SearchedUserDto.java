package ru.hits.musicservice.dto;

import lombok.*;
import ru.hits.musicservice.entity.UserEntity;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SearchedUserDto {

    private UUID id;

    private String username;

    private String name;

    private String surname;

    private String city;

    private String country;

    private String bio;

    private UUID avatarId;

    private int followersCount;

    private boolean isFollowing;

    public SearchedUserDto(UserEntity user, boolean isFollowing) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.city = user.getCity();
        this.country = user.getCountry();
        this.bio = user.getBio();
        this.avatarId = user.getAvatar();
        this.followersCount = user.getFollowersCount();
        this.isFollowing = isFollowing;
    }
}
