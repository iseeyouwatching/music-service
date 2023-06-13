package ru.hits.musicservice.dto;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FollowerInfoDto {

    private UUID id;

    private UUID avatarId;

    private String username;

    private int subscribersCount;

    private boolean isFollowing;

}
