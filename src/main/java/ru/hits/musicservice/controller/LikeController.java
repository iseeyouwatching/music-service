package ru.hits.musicservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hits.musicservice.dto.SongInfoDto;
import ru.hits.musicservice.service.LikeService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
@Tag(name = "Лайки.")
public class LikeController {

    private final LikeService likeService;

    @Operation(
            summary = "Поставить лайк на песню.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/add/{songId}")
    public ResponseEntity<Void> likeSong(@PathVariable("songId") UUID songId) {
        likeService.likeSong(songId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Убрать лайк с песни.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @DeleteMapping("/remove-like/{songId}")
    public ResponseEntity<Void> takeLikeOffTheSong(@PathVariable("songId") UUID songId) {
        likeService.takeLikeOffTheSong(songId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Получить лайкнутые пользователем песни.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/{userId}")
    public ResponseEntity<List<SongInfoDto>> getLikedSongs(@PathVariable("userId") UUID userId) {
        return new ResponseEntity<>(likeService.getLikedSongs(userId), HttpStatus.OK);
    }
}
