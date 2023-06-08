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
import ru.hits.musicservice.dto.FollowerDto;
import ru.hits.musicservice.service.FollowerService;

import java.util.UUID;

@RestController
@RequestMapping("/api/followers")
@RequiredArgsConstructor
@Tag(name = "Подписчики.")
public class FollowerController {

    private final FollowerService followerService;

    @Operation(
            summary = "Подписаться на артиста.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/follow-an-artist/{artistId}")
    public ResponseEntity<FollowerDto> followAnArtist(@PathVariable("artistId") UUID artistId) {
        return new ResponseEntity<>(followerService.followAnArtist(artistId), HttpStatus.OK);
    }

}
