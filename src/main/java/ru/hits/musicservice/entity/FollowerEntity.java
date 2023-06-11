package ru.hits.musicservice.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "follower")
public class FollowerEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Column(name = "following_date")
    private LocalDateTime followingDate;

    @Column(name = "unfollowing_date")
    private LocalDateTime unfollowingDate;

    @Column(name = "is_following")
    private boolean isFollowing;

    @Column(name = "artist_id")
    private UUID artistId;

    @Column(name = "follower_id")
    private UUID followerId;

}
