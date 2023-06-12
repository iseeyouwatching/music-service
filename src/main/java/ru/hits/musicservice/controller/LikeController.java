package ru.hits.musicservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hits.musicservice.service.LikeService;

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
}
