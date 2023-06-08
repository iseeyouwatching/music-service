package ru.hits.musicservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hits.musicservice.dto.FollowerDto;
import ru.hits.musicservice.entity.FollowerEntity;
import ru.hits.musicservice.exception.ConflictException;
import ru.hits.musicservice.exception.NotFoundException;
import ru.hits.musicservice.repository.FollowerRepository;
import ru.hits.musicservice.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FollowerService {

    private final FollowerRepository followerRepository;
    private final UserRepository userRepository;

    @Transactional
    public FollowerDto followAnArtist(UUID artistId) {
        if (userRepository.findById(artistId).isEmpty()) {
            throw new NotFoundException("Пользователя с ID " + artistId + " не существует.");
        }

        UUID followerId = getAuthenticatedUserId();
        if (followerId.compareTo(artistId) == 0) {
            throw new ConflictException("Пользователь не может подписаться на самого себя");
        }

        if (followerRepository.findByArtistIdAndFollowerId(artistId, followerId).isPresent()) {
            throw new ConflictException("Пользователь с ID " + followerId + " уже является подписчиком пользователя с ID "
                    + artistId);
        }

        FollowerEntity follower = FollowerEntity.builder()
                .followingDate(LocalDateTime.now())
                .unfollowingDate(null)
                .isFollowing(true)
                .artistId(artistId)
                .followerId(followerId)
                .build();
        follower = followerRepository.save(follower);

        return new FollowerDto(follower);
    }

    private UUID getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UUID) authentication.getPrincipal();
    }

}
