package ru.hits.musicservice.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.hits.musicservice.entity.SongEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SongRepository extends JpaRepository<SongEntity, UUID> {

    List<SongEntity> findAllByAuthorId(UUID authorId, Sort sort);

    Optional<SongEntity> findByIdAndAuthorId(UUID songId, UUID authorId);

    @Query("SELECT s FROM SongEntity s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<SongEntity> findByNameWildcard(@Param("name") String name);

}
