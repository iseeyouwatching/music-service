package ru.hits.musicservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hits.musicservice.dto.*;
import ru.hits.musicservice.service.SongService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

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
    public ResponseEntity<SongInfoDto> addSong(@RequestBody @Valid AddSongDto addSongDto) {
        return new ResponseEntity<>(songService.addSong(addSongDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Удалить песню.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable("id") UUID songId) {
        songService.deleteSong(songId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Получить загруженные пользователем песни.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/{userId}/uploaded-songs")
    public ResponseEntity<List<SongInfoDto>> getUploadedSongs(@PathVariable("userId") UUID userId) {
        return new ResponseEntity<>(songService.getUploadedSongs(userId), HttpStatus.OK);
    }

    @Operation(
            summary = "Получить информацию о песне по ID.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/{id}")
    public ResponseEntity<SongInfoDto> getSongInfo(@PathVariable("id") UUID songId) {
        return new ResponseEntity<>(songService.getSongInfo(songId), HttpStatus.OK);
    }

    @Operation(
            summary = "Поиск песен.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/search")
    public ResponseEntity<List<SearchedSongDto>> searchSongs(@RequestBody @Valid SearchStringDto searchStringDto) {
        return new ResponseEntity<>(songService.searchSongs(searchStringDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Обновление данных песни.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping("/{id}")
    public ResponseEntity<SongInfoDto> updateSongInfo(@PathVariable("id") UUID songId, @RequestBody @Valid UpdateSongInfoDto updateSongInfoDto) {
        return new ResponseEntity<>(songService.updateSongInfoDto(songId, updateSongInfoDto), HttpStatus.OK);
    }
}
