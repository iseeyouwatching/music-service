package ru.hits.musicservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hits.musicservice.dto.AddSongDto;
import ru.hits.musicservice.dto.SongInfoDto;
import ru.hits.musicservice.service.SongService;

@RestController
@RequestMapping("/api/songs")
@RequiredArgsConstructor
@Tag(name = "Песни.")
public class SongController {

    private final SongService songService;

    @Operation(
            summary = "Добавление информации о песне.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/add")
    public ResponseEntity<SongInfoDto> addSong(@RequestBody AddSongDto addSongDto) {
        return new ResponseEntity<>(songService.addSong(addSongDto), HttpStatus.OK);
    }

}
