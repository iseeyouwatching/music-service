package ru.hits.musicservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.hits.musicservice.dto.FollowerInfoDto;
import ru.hits.musicservice.entity.FollowerEntity;
import ru.hits.musicservice.entity.UserEntity;
import ru.hits.musicservice.repository.FollowerRepository;
import ru.hits.musicservice.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FollowersService {

    private final UserRepository userRepository;
    private final FollowerRepository followerRepository;

    public List<FollowerInfoDto> getFollowers(UUID userId) {
        Example<FollowerEntity> example = Example.of(FollowerEntity
                .builder()
                .isFollowing(true)
                .artistId(userId)
                .build());

        List<FollowerEntity> followerEntities =
                followerRepository.findAll(example, Sort.by(Sort.Direction.DESC, "followingDate"));

        List<FollowerInfoDto> followersResult = new ArrayList<>();

        for (FollowerEntity follower: followerEntities) {
            Optional<UserEntity> user = userRepository.findById(follower.getFollowerId());

            user.ifPresent(userEntity -> followersResult.add(new FollowerInfoDto(
                    userEntity.getId(),
                    userEntity.getAvatar(),
                    userEntity.getUsername(),
                    userEntity.getSubscribersCount())));
        }

        return followersResult;
    }

}
