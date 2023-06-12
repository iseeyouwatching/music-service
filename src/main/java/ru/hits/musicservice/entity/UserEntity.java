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

    @Column(name = "subscribers_count")
    private int subscribersCount;

}
