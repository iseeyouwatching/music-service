package ru.hits.musicservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.hits.musicservice.entity.UserEntity;
import ru.hits.musicservice.exception.TokenNotValidException;
import ru.hits.musicservice.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepository.findById(UUID.fromString(id));

        if (user.isEmpty()) {
            throw new TokenNotValidException("Невалидный токен.");
        }

        return new UserDetailsImpl(user.get());
    }
}
