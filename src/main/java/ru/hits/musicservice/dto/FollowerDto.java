package ru.hits.musicservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.hits.musicservice.entity.FollowerEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FollowerDto {

    private UUID id;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime followingDate;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime unfollowingDate;

    private UUID artistId;

    private UUID followerId;

    public FollowerDto(FollowerEntity follower) {
        this.id = follower.getId();
        this.followingDate = follower.getFollowingDate();
        this.unfollowingDate = follower.getUnfollowingDate();
        this.artistId = follower.getArtistId();
        this.followerId = follower.getFollowerId();
    }

}
