package ru.hits.musicservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.hits.musicservice.dto.SubscriberInfoDto;
import ru.hits.musicservice.entity.SubscriberEntity;
import ru.hits.musicservice.entity.UserEntity;
import ru.hits.musicservice.repository.SubscriberRepository;
import ru.hits.musicservice.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscribersService {

    private final UserRepository userRepository;
    private final SubscriberRepository subscriberRepository;

    public List<SubscriberInfoDto> getSubscribers() {
        UUID authenticatedUserId = getAuthenticatedUserId();

        Example<SubscriberEntity> example = Example.of(SubscriberEntity
                .builder()
                .isFollowing(true)
                .artistId(authenticatedUserId)
                .build());

        List<SubscriberEntity> subscriberEntities =
                subscriberRepository.findAll(example, Sort.by(Sort.Direction.DESC, "followingDate"));

        List<SubscriberInfoDto> subscribersResult = new ArrayList<>();

        for (SubscriberEntity subscriber: subscriberEntities) {
            Optional<UserEntity> user = userRepository.findById(subscriber.getFollowerId());

            user.ifPresent(userEntity -> subscribersResult.add(new SubscriberInfoDto(
                    userEntity.getId(),
                    userEntity.getAvatar(),
                    userEntity.getUsername(),
                    userEntity.getSubscribersCount())));
        }

        return subscribersResult;
    }

    private UUID getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UUID) authentication.getPrincipal();
    }

}
