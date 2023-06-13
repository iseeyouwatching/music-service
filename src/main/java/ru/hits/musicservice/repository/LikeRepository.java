package ru.hits.musicservice.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hits.musicservice.entity.LikeEntity;
import ru.hits.musicservice.entity.SongEntity;
import ru.hits.musicservice.entity.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, UUID> {

    Optional<LikeEntity> findByUserAndSong(UserEntity user, SongEntity song);

    List<LikeEntity> findAllByUser(UserEntity user, Sort sort);

    List<LikeEntity> findAllBySong(SongEntity song);

}
