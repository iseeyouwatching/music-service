package ru.hits.musicservice.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import ru.hits.musicservice.enumeration.NotificationStatus;
import ru.hits.musicservice.enumeration.NotificationType;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "notification")
public class NotificationEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    private NotificationType type;

    private String text;

    @Column(name = "user_id")
    private UUID userId;

    private NotificationStatus status;

    @Column(name = "send_date")
    private LocalDateTime sendDate;

}
