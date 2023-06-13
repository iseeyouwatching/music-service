package ru.hits.musicservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hits.musicservice.dto.SongInfoDto;
import ru.hits.musicservice.entity.LikeEntity;
import ru.hits.musicservice.entity.NotificationEntity;
import ru.hits.musicservice.entity.SongEntity;
import ru.hits.musicservice.entity.UserEntity;
import ru.hits.musicservice.enumeration.NotificationType;
import ru.hits.musicservice.exception.ConflictException;
import ru.hits.musicservice.exception.NotFoundException;
import ru.hits.musicservice.repository.LikeRepository;
import ru.hits.musicservice.repository.NotificationRepository;
import ru.hits.musicservice.repository.SongRepository;
import ru.hits.musicservice.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final SongRepository songRepository;
    private final NotificationRepository notificationRepository;

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
                .likeDate(LocalDateTime.now())
                .build();
        likeRepository.save(like);

        song.setLikesCount(song.getLikesCount() + 1);
        song = songRepository.save(song);

        user.setLikesCount(user.getLikesCount() + 1);
        userRepository.save(user);

        NotificationEntity notification = NotificationEntity.builder()
                .type(NotificationType.LIKE_SONG)
                .text("Пользователь с ID " + authenticatedUserId + " лайкнул трек с ID " + song.getId() + ".")
                .userId(song.getAuthorId())
                .sendDate(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);
    }

    @Transactional
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
            song = songRepository.save(song);
        }

        if (user.getLikesCount() != 0) {
            user.setLikesCount(user.getLikesCount() - 1);
            userRepository.save(user);
        }

        notificationRepository.deleteByText("Пользователь с ID " + authenticatedUserId
                + " лайкнул трек с ID " + song.getId() + ".");
    }

    public List<SongInfoDto> getLikedSongs(UUID userId) {
        Optional<UserEntity> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь с ID " + userId + " не найден.");
        }

        List<LikeEntity> likes = likeRepository.findAllByUser(user.get(),
                Sort.by(Sort.Direction.DESC, "likeDate"));

        List<SongInfoDto> result = new ArrayList<>();
        for (LikeEntity like: likes) {
            result.add(new SongInfoDto(like.getSong()));
        }

        return result;
    }

    private UUID getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UUID) authentication.getPrincipal();
    }

}
