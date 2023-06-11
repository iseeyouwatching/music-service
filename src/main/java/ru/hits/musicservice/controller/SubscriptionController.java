package ru.hits.musicservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hits.musicservice.dto.SubscriberDto;
import ru.hits.musicservice.service.SubscriptionService;

import java.util.UUID;

@RestController
@RequestMapping("/api/subscription")
@RequiredArgsConstructor
@Tag(name = "Подписки.")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Operation(
            summary = "Подписаться на пользователя.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/subscribe/{userId}")
    public ResponseEntity<SubscriberDto> subscribe(@PathVariable("userId") UUID userId) {
        return new ResponseEntity<>(subscriptionService.subscribe(userId), HttpStatus.OK);
    }

    @Operation(
            summary = "Отписаться от пользователя.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/unsubscribe/{userId}")
    public ResponseEntity<SubscriberDto> unsubscribe(@PathVariable("userId") UUID userId) {
        return new ResponseEntity<>(subscriptionService.unsubscribe(userId), HttpStatus.OK);
    }

}
