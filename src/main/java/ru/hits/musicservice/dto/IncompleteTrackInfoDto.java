package ru.hits.musicservice.dto;

import lombok.*;
import ru.hits.musicservice.entity.TrackEntity;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class IncompleteTrackInfoDto {

    private UUID id;

    private String uploaderUsername;

    private String name;

    private Integer likesCounter;

    private boolean isPublic;

    public IncompleteTrackInfoDto(TrackEntity track) {
        this.id = track.getId();
        this.uploaderUsername = track.getUploaderUsername();
        this.name = track.getName();
        this.likesCounter = track.getLikesCount();
        this.isPublic = track.isPublic();
    }

}
