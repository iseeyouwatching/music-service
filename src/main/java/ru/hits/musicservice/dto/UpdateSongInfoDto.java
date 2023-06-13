package ru.hits.musicservice.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateSongInfoDto {

    private UUID coverId;

    private String name;

    private String description;

}
