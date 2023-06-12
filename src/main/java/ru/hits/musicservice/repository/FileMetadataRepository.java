package ru.hits.musicservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hits.musicservice.entity.FileMetadataEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FileMetadataRepository extends JpaRepository<FileMetadataEntity, UUID> {

    Optional<FileMetadataEntity> findByObjectName(UUID id);

    void deleteByObjectName(UUID objectName);

}
