package ru.hits.musicservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hits.musicservice.entity.BlacklistTokenEntity;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface BlacklistTokenRepository extends JpaRepository<BlacklistTokenEntity, UUID> {

    Optional<BlacklistTokenEntity> findByToken(String token);

}
