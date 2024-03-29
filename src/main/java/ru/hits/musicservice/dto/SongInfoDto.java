package ru.hits.musicservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.hits.musicservice.entity.SongEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SongInfoDto {

    private UUID id;

    private UUID coverId;

    private String name;

    private String authorUsername;

    private UUID authorId;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime uploadDate;

    private String description;

    private int likesCount;

    private UUID fileId;

    private boolean isLiked;

    public SongInfoDto(SongEntity song, boolean isLiked) {
        this.id = song.getId();
        this.coverId = song.getCoverId();
        this.name = song.getName();
        this.authorUsername = song.getAuthorUsername();
        this.authorId = song.getAuthorId();
        this.uploadDate = song.getUploadDate();
        this.description = song.getDescription();
        this.likesCount = song.getLikesCount();
        this.fileId = song.getFileId();
        this.isLiked = isLiked;
    }

}
