package ru.hits.musicservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hits.musicservice.entity.TrackEntity;
import ru.hits.musicservice.entity.UserEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface TrackRepository extends JpaRepository<TrackEntity, UUID> {

    List<TrackEntity> findAllByUser(UserEntity user);

}
