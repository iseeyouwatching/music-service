package ru.hits.musicservice.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hits.musicservice.dto.AddSongDto;
import ru.hits.musicservice.dto.SongInfoDto;
import ru.hits.musicservice.entity.SongEntity;
import ru.hits.musicservice.exception.NotFoundException;
import ru.hits.musicservice.repository.FileMetadataRepository;
import ru.hits.musicservice.repository.SongRepository;
import ru.hits.musicservice.repository.UserRepository;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SongService {

    private final SongRepository songRepository;

    private final UserRepository userRepository;

    private final FileMetadataRepository fileMetadataRepository;

    private final ModelMapper modelMapper;

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
                .uploadDate(LocalDate.now())
                .description(addSongDto.getDescription())
                .likesCount(0)
                .fileId(addSongDto.getFileId())
                .build();

        song = songRepository.save(song);

        return new SongInfoDto(song);
    }

    private UUID getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UUID) authentication.getPrincipal();
    }

}
