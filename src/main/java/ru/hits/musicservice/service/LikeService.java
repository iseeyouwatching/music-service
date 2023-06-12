package ru.hits.musicservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hits.musicservice.entity.LikeEntity;
import ru.hits.musicservice.entity.SongEntity;
import ru.hits.musicservice.entity.UserEntity;
import ru.hits.musicservice.exception.ConflictException;
import ru.hits.musicservice.exception.NotFoundException;
import ru.hits.musicservice.repository.LikeRepository;
import ru.hits.musicservice.repository.SongRepository;
import ru.hits.musicservice.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final SongRepository songRepository;

    @Transactional
    public void likeSong(UUID songId) {
        UUID authenticatedUserId = getAuthenticatedUserId();

        UserEntity user = userRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new NotFoundException("Пользователя с ID " + authenticatedUserId + " не существует."));
        SongEntity song = songRepository.findById(songId)
                .orElseThrow(() -> new NotFoundException("Песни с ID " + songId + " не существует."));

        if (likeRepository.findByUserAndSong(user, song).isPresent()) {
            throw new ConflictException("У пользователя с ID " + authenticatedUserId
                    + " уже стоит лайк на песне с ID " + song.getId() + ".");
        }

        LikeEntity like = LikeEntity.builder()
                .user(user)
                .song(song)
                .build();
        likeRepository.save(like);
        song.setLikesCount(song.getLikesCount() + 1);
        songRepository.save(song);
    }

    public void takeLikeOffTheSong(UUID songId) {
        UUID authenticatedUserId = getAuthenticatedUserId();

        UserEntity user = userRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new NotFoundException("Пользователя с ID " + authenticatedUserId + " не существует."));
        SongEntity song = songRepository.findById(songId)
                .orElseThrow(() -> new NotFoundException("Песни с ID " + songId + " не существует."));

        Optional<LikeEntity> like = likeRepository.findByUserAndSong(user, song);
        if (like.isEmpty()) {
            throw new ConflictException("У пользователя с ID " + authenticatedUserId
                    + " не стоит лайк на песне с ID " + song.getId() + ".");
        }

        likeRepository.delete(like.get());

        if (song.getLikesCount() != 0) {
            song.setLikesCount(song.getLikesCount() - 1);
            songRepository.save(song);
        }
    }

    private UUID getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UUID) authentication.getPrincipal();
    }

}
