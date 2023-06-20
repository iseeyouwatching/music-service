package ru.hits.musicservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hits.musicservice.dto.NotificationDto;
import ru.hits.musicservice.service.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Уведомления.")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(
            summary = "Получить список уведомлений.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/get")
    public ResponseEntity<List<NotificationDto>> getNotifications() {
        return new ResponseEntity<>(notificationService.getNotifications(), HttpStatus.OK);
    }

    @Operation(
            summary = "Получить количество непрочитанных уведомлений.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount() {
        return new ResponseEntity<>(notificationService.getUnreadCount(), HttpStatus.OK);
    }

    @Operation(
            summary = "Пометить уведомления как прочитанные.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping("/mark-as-read")
    public ResponseEntity<Long> markAsReadOrUnread() {
        return new ResponseEntity<>(notificationService.markNotificationsAsRead(), HttpStatus.OK);
    }

}
