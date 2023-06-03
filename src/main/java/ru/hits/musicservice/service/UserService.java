package ru.hits.musicservice.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hits.musicservice.dto.UserProfileAndTokenDto;
import ru.hits.musicservice.dto.UserProfileDto;
import ru.hits.musicservice.dto.UserSignInDto;
import ru.hits.musicservice.dto.UserSignUpDto;
import ru.hits.musicservice.entity.UserEntity;
import ru.hits.musicservice.exception.ConflictException;
import ru.hits.musicservice.exception.NotFoundException;
import ru.hits.musicservice.exception.UnauthorizedException;
import ru.hits.musicservice.repository.UserRepository;
import ru.hits.musicservice.security.JWTUtil;

import java.util.Optional;
import java.util.UUID;

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

    public UserProfileAndTokenDto userSignIn(UserSignInDto userSignInDto) {
        Optional<UserEntity> user = userRepository.findByEmail(userSignInDto.getEmail());

        if (user.isEmpty() ||
                !bCryptPasswordEncoder.matches(userSignInDto.getPassword(), user.get().getPassword())) {
            throw new UnauthorizedException("Некорректные данные.");
        }

        return new UserProfileAndTokenDto(new UserProfileDto(user.get()), jwtUtil.generateToken(user.get().getId()));
    }

    public UserProfileDto getUserProfileInfo() {
        UUID id = getAuthenticatedUserId();
        Optional<UserEntity> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь с ID " + id + " не найден.");
        }

        return new UserProfileDto(user.get());
    }

    private UUID getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UUID) authentication.getPrincipal();
    }

}
