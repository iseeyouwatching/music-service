package ru.hits.musicservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hits.musicservice.dto.NotificationDto;
import ru.hits.musicservice.entity.NotificationEntity;
import ru.hits.musicservice.enumeration.NotificationStatus;
import ru.hits.musicservice.repository.NotificationRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public List<NotificationDto> getNotifications() {
        UUID authenticatedUserId = getAuthenticatedUserId();

        List<NotificationEntity> notifications = notificationRepository.findAllByUserId(authenticatedUserId,
                Sort.by(Sort.Direction.DESC, "sendDate"));

        List<NotificationDto> result = new ArrayList<>();
        for (NotificationEntity notification: notifications) {
            result.add(new NotificationDto(notification));
        }

        return result;
    }

    public Long getUnreadCount() {
        return notificationRepository.countByUserIdAndStatus(getAuthenticatedUserId(), NotificationStatus.UNREAD);
    }

    @Transactional
    public Long markNotificationsAsRead() {
        notificationRepository.updateStatusByUserId(getAuthenticatedUserId(), NotificationStatus.READ);
        return getUnreadCount();
    }

    private UUID getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UUID) authentication.getPrincipal();
    }

}
