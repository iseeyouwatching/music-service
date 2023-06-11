package ru.hits.musicservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hits.musicservice.dto.SubscriberDto;
import ru.hits.musicservice.entity.SubscriberEntity;
import ru.hits.musicservice.exception.ConflictException;
import ru.hits.musicservice.exception.NotFoundException;
import ru.hits.musicservice.repository.SubscriberRepository;
import ru.hits.musicservice.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriberRepository subscriberRepository;
    private final UserRepository userRepository;

    @Transactional
    public SubscriberDto subscribe(UUID userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("Пользователя с ID " + userId + " не существует.");
        }

        UUID followerId = getAuthenticatedUserId();
        if (followerId.compareTo(userId) == 0) {
            throw new ConflictException("Пользователь не может подписаться на самого себя.");
        }

        Optional<SubscriberEntity> subscriber = subscriberRepository.findByArtistIdAndFollowerId(userId, followerId);
        if (subscriber.isPresent() && subscriber.get().isFollowing()) {
            throw new ConflictException("Пользователь с ID " + followerId + " уже является подписчиком пользователя с ID "
                    + userId + ".");
        }

        SubscriberEntity subscriberResult = SubscriberEntity.builder()
                .followingDate(LocalDateTime.now())
                .unfollowingDate(null)
                .isFollowing(true)
                .artistId(userId)
                .followerId(followerId)
                .build();
        subscriberResult = subscriberRepository.save(subscriberResult);

        return new SubscriberDto(subscriberResult);
    }

    @Transactional
    public SubscriberDto unsubscribe(UUID userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("Пользователя с ID " + userId + " не существует.");
        }

        UUID followerId = getAuthenticatedUserId();
        if (followerId.compareTo(userId) == 0) {
            throw new ConflictException("Пользователь не может отписаться от самого себя.");
        }

        Optional<SubscriberEntity> subscriber = subscriberRepository.findByArtistIdAndFollowerId(userId, followerId);
        if (subscriber.isEmpty() || !subscriber.get().isFollowing()) {
            throw new ConflictException("Пользователь с ID " + followerId + " не является подписчиком пользователя с ID "
                    + userId + ".");
        }

        subscriber.get().setUnfollowingDate(LocalDateTime.now());
        subscriber.get().setFollowingDate(null);
        subscriber.get().setFollowing(false);
        subscriber = Optional.of(subscriberRepository.save(subscriber.get()));

        return new SubscriberDto(subscriber.get());
    }

    private UUID getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UUID) authentication.getPrincipal();
    }

}
