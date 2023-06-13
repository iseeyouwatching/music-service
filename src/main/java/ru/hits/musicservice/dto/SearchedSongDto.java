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
public class SearchedSongDto {

    private UUID id;

    private UUID coverId;

    private String name;

    private String authorUsername;

    private UUID authorId;

    private String description;

    private int likesCount;

    private UUID fileId;

    private boolean isLiked;

    public SearchedSongDto(SongEntity song, boolean isLiked) {
        this.id = song.getId();
        this.coverId = song.getCoverId();
        this.name = song.getName();
        this.authorUsername = song.getAuthorUsername();
        this.authorId = song.getAuthorId();
        this.description = song.getDescription();
        this.likesCount = song.getLikesCount();
        this.fileId = song.getFileId();
        this.isLiked = isLiked;
    }

}
