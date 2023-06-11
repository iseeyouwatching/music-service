package ru.hits.musicservice.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import ru.hits.musicservice.enumeration.Gender;

import javax.persistence.*;
import java.util.List;
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

    private UUID headerImage;

    private int subscribersCount;

    @OneToMany(mappedBy = "user")
    private List<TrackEntity> tracks;

}
