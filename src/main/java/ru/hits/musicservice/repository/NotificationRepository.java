package ru.hits.musicservice.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.hits.musicservice.entity.NotificationEntity;
import ru.hits.musicservice.enumeration.NotificationStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, UUID> {

    List<NotificationEntity> findAllByUserId(UUID userId, Sort sort);

    Long countByUserIdAndStatus(UUID userId, NotificationStatus status);

    @Transactional
    @Modifying
    @Query("UPDATE NotificationEntity n SET n.status = :status WHERE n.userId = :userId")
    void updateStatusByUserId(UUID userId, NotificationStatus status);

    void deleteByText(String text);

}
