package ru.hits.musicservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hits.musicservice.dto.FollowerDto;
import ru.hits.musicservice.dto.FollowerInfoDto;
import ru.hits.musicservice.dto.FollowingUserInfoDto;
import ru.hits.musicservice.entity.FollowerEntity;
import ru.hits.musicservice.entity.UserEntity;
import ru.hits.musicservice.exception.ConflictException;
import ru.hits.musicservice.exception.NotFoundException;
import ru.hits.musicservice.repository.FollowerRepository;
import ru.hits.musicservice.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FollowingService {

    private final FollowerRepository followerRepository;
    private final UserRepository userRepository;

    @Transactional
    public FollowerDto subscribe(UUID userId) {
        Optional<UserEntity> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователя с ID " + userId + " не существует.");
        }

        UUID followerId = getAuthenticatedUserId();
        if (followerId.compareTo(userId) == 0) {
            throw new ConflictException("Пользователь не может подписаться на самого себя.");
        }

        Optional<FollowerEntity> subscriber = followerRepository.findByArtistIdAndFollowerId(userId, followerId);
        if (subscriber.isPresent() && subscriber.get().isFollowing()) {
            throw new ConflictException("Пользователь с ID " + followerId + " уже является подписчиком пользователя с ID "
                    + userId + ".");
        }

        FollowerEntity subscriberResult = FollowerEntity.builder()
                .followingDate(LocalDateTime.now())
                .unfollowingDate(null)
                .isFollowing(true)
                .artistId(userId)
                .followerId(followerId)
                .build();
        subscriberResult = followerRepository.save(subscriberResult);
        user.get().setSubscribersCount(user.get().getSubscribersCount() + 1);
        userRepository.save(user.get());

        return new FollowerDto(subscriberResult);
    }

    @Transactional
    public FollowerDto unsubscribe(UUID userId) {
        Optional<UserEntity> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователя с ID " + userId + " не существует.");
        }

        UUID followerId = getAuthenticatedUserId();
        if (followerId.compareTo(userId) == 0) {
            throw new ConflictException("Пользователь не может отписаться от самого себя.");
        }

        Optional<FollowerEntity> subscriber = followerRepository.findByArtistIdAndFollowerId(userId, followerId);
        if (subscriber.isEmpty() || !subscriber.get().isFollowing()) {
            throw new ConflictException("Пользователь с ID " + followerId + " не является подписчиком пользователя с ID "
                    + userId + ".");
        }

        subscriber.get().setUnfollowingDate(LocalDateTime.now());
        subscriber.get().setFollowingDate(null);
        subscriber.get().setFollowing(false);
        subscriber = Optional.of(followerRepository.save(subscriber.get()));

        if (user.get().getSubscribersCount() != 0) {
            user.get().setSubscribersCount(user.get().getSubscribersCount() - 1);
            userRepository.save(user.get());
        }

        return new FollowerDto(subscriber.get());
    }

    public List<FollowingUserInfoDto> getFollowings(UUID userId) {
        Example<FollowerEntity> example = Example.of(FollowerEntity
                .builder()
                .isFollowing(true)
                .followerId(userId)
                .build());

        List<FollowerEntity> followings =
                followerRepository.findAll(example, Sort.by(Sort.Direction.DESC, "followingDate"));

        List<FollowingUserInfoDto> followingUserInfoDtos = new ArrayList<>();

        for (FollowerEntity following: followings) {
            Optional<UserEntity> user = userRepository.findById(following.getArtistId());

            user.ifPresent(userEntity -> followingUserInfoDtos.add(new FollowingUserInfoDto(
                    userEntity.getId(),
                    userEntity.getAvatar(),
                    userEntity.getUsername(),
                    userEntity.getSubscribersCount())));
        }

        return followingUserInfoDtos;
    }

    private UUID getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UUID) authentication.getPrincipal();
    }

}
