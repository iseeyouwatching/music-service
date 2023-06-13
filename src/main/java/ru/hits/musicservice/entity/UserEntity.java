package ru.hits.musicservice.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import ru.hits.musicservice.enumeration.Gender;

import javax.persistence.*;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "_user")
public class UserEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(unique = true)
    private String username;

    private String name;

    private String surname;

    private String city;

    private String country;

    private int age;

    private Gender gender;

    private String bio;

    private UUID avatar;

    @Column(name = "header_image")
    private UUID headerImage;

    @Column(name = "followers_count")
    private int followersCount;

    @Column(name = "following_count")
    private int followingCount;

    @Column(name = "likes_count")
    private int likesCount;

    @Column(name = "uploaded_songs_count")
    private int uploadedSongsCount;

}
