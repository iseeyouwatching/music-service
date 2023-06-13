package ru.hits.musicservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hits.musicservice.dto.*;
import ru.hits.musicservice.entity.LikeEntity;
import ru.hits.musicservice.entity.SongEntity;
import ru.hits.musicservice.entity.UserEntity;
import ru.hits.musicservice.exception.ConflictException;
import ru.hits.musicservice.exception.NotFoundException;
import ru.hits.musicservice.repository.FileMetadataRepository;
import ru.hits.musicservice.repository.LikeRepository;
import ru.hits.musicservice.repository.SongRepository;
import ru.hits.musicservice.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SongService {

    private final SongRepository songRepository;
    private final UserRepository userRepository;
    private final FileMetadataRepository fileMetadataRepository;
    private final LikeRepository likeRepository;

    @Transactional
    public SongInfoDto addSong(AddSongDto addSongDto) {
        UUID authorId = getAuthenticatedUserId();

        if (fileMetadataRepository.findByObjectName(addSongDto.getFileId()).isEmpty() ||
                fileMetadataRepository.findByObjectName(addSongDto.getCoverId()).isEmpty()) {
            throw new NotFoundException("Файла с ID " + addSongDto.getFileId() + " не существует.");
        }

        SongEntity song = SongEntity.builder()
                .coverId(addSongDto.getCoverId())
                .name(addSongDto.getName())
                .authorUsername(userRepository.findById(authorId).get().getUsername())
                .authorId(authorId)
                .uploadDate(LocalDateTime.now())
                .description(addSongDto.getDescription())
                .likesCount(0)
                .fileId(addSongDto.getFileId())
                .build();

        song = songRepository.save(song);

        UserEntity uploader = userRepository.findById(authorId).get();
        uploader.setUploadedSongsCount(uploader.getUploadedSongsCount() + 1);
        userRepository.save(uploader);

        return new SongInfoDto(song, false);
    }

    @Transactional
    public void deleteSong(UUID songId) {
        UUID authorId = getAuthenticatedUserId();

        if (songRepository.findById(songId).isEmpty()) {
            throw new NotFoundException("Песни с ID " + songId + " не существует.");
        }

        Optional<SongEntity> song = songRepository.findByIdAndAuthorId(songId, authorId);
        if (song.isEmpty()) {
            throw new NotFoundException("У пользователя с ID " + authorId + " нет песни с ID " + songId + ".");
        }

        List<LikeEntity> likes = likeRepository.findAllBySong(song.get());
        for (LikeEntity like: likes) {
            UserEntity user = like.getUser();
            if (user.getLikesCount() != 0) {
                user.setLikesCount(user.getLikesCount() - 1);
                userRepository.save(user);
            }
        }

        fileMetadataRepository.deleteByObjectName(song.get().getFileId());
        fileMetadataRepository.deleteByObjectName(song.get().getCoverId());
        songRepository.delete(song.get());

        UserEntity uploader = userRepository.findById(authorId).get();
        if (uploader.getUploadedSongsCount() != 0) {
            uploader.setUploadedSongsCount(uploader.getUploadedSongsCount() - 1);
            userRepository.save(uploader);
        }
    }

    public List<SongInfoDto> getUploadedSongs(UUID userId) {
        Optional<UserEntity> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь с ID " + userId + " не найден.");
        }

        List<SongEntity> songs = songRepository.findAllByAuthorId(userId,
                Sort.by(Sort.Direction.DESC, "uploadDate"));

        List<SongInfoDto> result = new ArrayList<>();
        for (SongEntity song : songs) {
            if (likeRepository.findByUserAndSong(userRepository.findById(getAuthenticatedUserId()).get(), song).isPresent()) {
                result.add(new SongInfoDto(song, true));
            } else {
                result.add(new SongInfoDto(song, false));
            }
        }

        return result;
    }

    public List<SearchedSongDto> searchSongs(SearchStringDto searchStringDto) {
        UUID authenticatedUserId = getAuthenticatedUserId();

        List<SongEntity> searchedSongs =
                songRepository.findByNameWildcard(searchStringDto.getSearchString().toLowerCase());

        List<SearchedSongDto> result = new ArrayList<>();
        for (SongEntity song: searchedSongs) {
            if (likeRepository.findByUserAndSong(userRepository.findById(authenticatedUserId).get(), song).isPresent()) {
                result.add(new SearchedSongDto(song, true));
            } else {
                result.add(new SearchedSongDto(song, false));
            }
        }

        result.sort(Comparator.comparing(SearchedSongDto::isLiked, Comparator.reverseOrder()));

        return result;
    }

    public SongInfoDto getSongInfo(UUID songId) {
        Optional<SongEntity> song = songRepository.findById(songId);

        if (song.isEmpty()) {
            throw new NotFoundException("Песни с ID " + songId + " не существует.");
        }

        if (likeRepository.findByUserAndSong(userRepository.findById(getAuthenticatedUserId()).get(), song.get()).isPresent()) {
            return new SongInfoDto(song.get(), true);
        } else {
            return new SongInfoDto(song.get(), false);
        }
    }

    public SongInfoDto updateSongInfoDto(UUID songId, UpdateSongInfoDto updateSongInfoDto) {
        if (songRepository.findById(songId).isEmpty()) {
            throw new NotFoundException("Песни с ID " + songId + " не существует.");
        }

        UUID authenticatedUserId = getAuthenticatedUserId();
        Optional<SongEntity> song = songRepository.findByIdAndAuthorId(songId, authenticatedUserId);
        if (song.isEmpty()) {
            throw new ConflictException("Песня с ID " + songId + " не принадлежит пользователю с ID " + authenticatedUserId + ".");
        }

        if (updateSongInfoDto.getCoverId() != null) {
            if (fileMetadataRepository.findByObjectName(updateSongInfoDto.getCoverId()).isEmpty()) {
                throw new NotFoundException("Файла с ID " + updateSongInfoDto.getCoverId() + " не существует.");
            }
            song.get().setCoverId(updateSongInfoDto.getCoverId());
        }

        if (updateSongInfoDto.getName() != null) {
            song.get().setName(updateSongInfoDto.getName());
        }

        if (updateSongInfoDto.getDescription() != null) {
            song.get().setDescription(updateSongInfoDto.getDescription());
        }

        song = Optional.of(songRepository.save(song.get()));

        if (likeRepository.findByUserAndSong(userRepository.findById(getAuthenticatedUserId()).get(), song.get()).isPresent()) {
            return new SongInfoDto(song.get(), true);
        } else {
            return new SongInfoDto(song.get(), false);
        }
    }

    private UUID getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UUID) authentication.getPrincipal();
    }

}
