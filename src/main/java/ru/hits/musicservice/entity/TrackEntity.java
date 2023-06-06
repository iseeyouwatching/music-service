package ru.hits.musicservice.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "track")
public class TrackEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    private String uploaderUsername;

    private String name;

    private Integer likesCounter;

    private String description;

    private boolean isPublic;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
