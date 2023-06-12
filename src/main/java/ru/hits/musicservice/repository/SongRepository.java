package ru.hits.musicservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hits.musicservice.entity.SongEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SongRepository extends JpaRepository<SongEntity, UUID> {

    Optional<SongEntity> findByIdAndAuthorId(UUID songId, UUID authorId);

    void deleteByAuthorIdAndId(UUID authorId, UUID songId);

}
