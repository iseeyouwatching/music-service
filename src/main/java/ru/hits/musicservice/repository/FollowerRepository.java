package ru.hits.musicservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hits.musicservice.entity.FollowerEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FollowerRepository extends JpaRepository<FollowerEntity, UUID> {

    Optional<FollowerEntity> findByArtistIdAndFollowerId(UUID artistId, UUID followerId);

}
