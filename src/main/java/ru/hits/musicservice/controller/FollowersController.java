package ru.hits.musicservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hits.musicservice.dto.FollowerInfoDto;
import ru.hits.musicservice.service.FollowersService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/subscribers")
@RequiredArgsConstructor
@Tag(name = "Подписчики.")
public class FollowersController {

    private final FollowersService followersService;

    @Operation(
            summary = "Получить список подписчиков пользователя.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/{userId}")
    public ResponseEntity<List<FollowerInfoDto>> getFollowers(@PathVariable("userId") UUID userId) {
        return new ResponseEntity<>(followersService.getFollowers(userId), HttpStatus.OK);
    }

}
