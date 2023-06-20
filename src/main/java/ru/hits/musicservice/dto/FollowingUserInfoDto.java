package ru.hits.musicservice.dto;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FollowingUserInfoDto {

    private UUID id;

    private UUID avatarId;

    private String username;

    private int followersCount;

    private boolean isFollowing;

}
