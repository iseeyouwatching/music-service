package ru.hits.musicservice.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddSongDto {

    @NotNull(message = "Идентификатор обложки не может быть пустым.")
    private UUID coverId;

    @NotBlank(message = "Название песни не может быть пустым.")
    private String name;

    private String description;

    @NotNull(message = "Идентификатор файла не может быть пустым.")
    private UUID fileId;

}
