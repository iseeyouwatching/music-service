package ru.hits.musicservice.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hits.musicservice.dto.UserProfileAndTokenDto;
import ru.hits.musicservice.dto.UserProfileDto;
import ru.hits.musicservice.dto.UserSignUpDto;
import ru.hits.musicservice.entity.UserEntity;
import ru.hits.musicservice.exception.ConflictException;
import ru.hits.musicservice.repository.UserRepository;
import ru.hits.musicservice.security.JWTUtil;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;
    private final JWTUtil jwtUtil;

    @Transactional
    public UserProfileAndTokenDto userSignUp(UserSignUpDto userSignUpDto) {
        if (userRepository.findByUsername(userSignUpDto.getUsername()).isPresent()) {
            throw new ConflictException("Пользователь с ником " + userSignUpDto.getUsername() + " уже существует.");
        }

        if (userRepository.findByEmail(userSignUpDto.getEmail()).isPresent()) {
            throw new ConflictException("Пользователь с почтой " + userSignUpDto.getEmail() + " уже существует.");
        }

        UserEntity user = modelMapper.map(userSignUpDto, UserEntity.class);
        user.setPassword(bCryptPasswordEncoder.encode(userSignUpDto.getPassword()));
        user = userRepository.save(user);

        return new UserProfileAndTokenDto(new UserProfileDto(user), jwtUtil.generateToken(user.getId()));
    }

}
