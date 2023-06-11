package ru.hits.musicservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hits.musicservice.entity.SubscriberEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriberRepository extends JpaRepository<SubscriberEntity, UUID> {

    Optional<SubscriberEntity> findByArtistIdAndFollowerId(UUID userId, UUID followerId);

}
