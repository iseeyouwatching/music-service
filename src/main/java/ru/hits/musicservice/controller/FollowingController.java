package ru.hits.musicservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hits.musicservice.dto.FollowerDto;
import ru.hits.musicservice.dto.FollowingUserInfoDto;
import ru.hits.musicservice.service.FollowingService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/following")
@RequiredArgsConstructor
@Tag(name = "Подписки.")
public class FollowingController {

    private final FollowingService followingService;

    @Operation(
            summary = "Подписаться на пользователя.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/follow/{userId}")
    public ResponseEntity<FollowerDto> follow(@PathVariable("userId") UUID userId) {
        return new ResponseEntity<>(followingService.subscribe(userId), HttpStatus.OK);
    }

    @Operation(
            summary = "Отписаться от пользователя.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/unfollow/{userId}")
    public ResponseEntity<FollowerDto> unfollow(@PathVariable("userId") UUID userId) {
        return new ResponseEntity<>(followingService.unsubscribe(userId), HttpStatus.OK);
    }

    @Operation(
            summary = "Получить список подписок пользователя.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/{userId}")
    public ResponseEntity<List<FollowingUserInfoDto>> getFollowings(@PathVariable("userId") UUID userId) {
        return new ResponseEntity<>(followingService.getFollowings(userId), HttpStatus.OK);
    }

}
