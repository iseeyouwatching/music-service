package ru.hits.musicservice.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "song")
public class SongEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Column(name = "cover_id")
    private UUID coverId;

    private String name;

    @Column(name = "author_username")
    private String authorUsername;

    @Column(name = "author_id")
    private UUID authorId;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate;

    private String description;

    @Column(name = "likes_count")
    private int likesCount;

    @Column(name = "file_id")
    private UUID fileId;

    @OneToMany(mappedBy = "song", cascade = CascadeType.REMOVE)
    private List<LikeEntity> likes;

}
