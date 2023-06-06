package ru.hits.musicservice.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hits.musicservice.dto.*;
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

    @Transactional
    public UserProfileDto updateUserInfo(UserUpdateInfoDto userUpdateInfoDto) {
        UUID id = getAuthenticatedUserId();
        Optional<UserEntity> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь с ID " + id + " не найден.");
        }

        updateUserEntity(user.get(), userUpdateInfoDto);

        UserEntity savedUser = userRepository.save(user.get());
        return new UserProfileDto(savedUser);
    }

    private void updateUserEntity(UserEntity user, UserUpdateInfoDto userUpdateInfoDto) {
        if (userUpdateInfoDto.getAvatar() != null) {
            user.setAvatar(userUpdateInfoDto.getAvatar());
        }

        if (userUpdateInfoDto.getUsername() != null) {
            user.setUsername(userUpdateInfoDto.getUsername());
        }

        if (userUpdateInfoDto.getName() != null) {
            user.setName(userUpdateInfoDto.getName());
        }

        if (userUpdateInfoDto.getSurname() != null) {
            user.setSurname(userUpdateInfoDto.getSurname());
        }

        if (userUpdateInfoDto.getCity() != null) {
            user.setCity(userUpdateInfoDto.getCity());
        }

        if (userUpdateInfoDto.getCountry() != null) {
            user.setCountry(userUpdateInfoDto.getCountry());
        }

        if (userUpdateInfoDto.getBio() != null) {
            user.setBio(userUpdateInfoDto.getBio());
        }
    }

    private UUID getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UUID) authentication.getPrincipal();
    }

}
