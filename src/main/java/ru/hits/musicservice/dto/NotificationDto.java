package ru.hits.musicservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.hits.musicservice.entity.NotificationEntity;
import ru.hits.musicservice.enumeration.NotificationType;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificationDto {

    private UUID id;

    private NotificationType type;

    private String text;

    private UUID userId;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime sendDate;

    public NotificationDto(NotificationEntity notification) {
        this.id = notification.getId();
        this.type = notification.getType();
        this.text = notification.getText();
        this.userId = notification.getUserId();
        this.sendDate = notification.getSendDate();
    }

}
