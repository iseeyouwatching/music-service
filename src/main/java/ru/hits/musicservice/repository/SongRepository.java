package ru.hits.musicservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hits.musicservice.entity.SongEntity;

import java.util.UUID;

@Repository
public interface SongRepository extends JpaRepository<SongEntity, UUID> {
}
