package ru.hits.musicservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hits.musicservice.dto.*;
import ru.hits.musicservice.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Пользователь.")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Регистрация.")
    @PostMapping("/register")
    public ResponseEntity<UserProfileDto> userSignUp(@RequestBody @Valid UserSignUpDto userSignUpDto) {
        UserProfileAndTokenDto userProfileAndTokenDto = userService.userSignUp(userSignUpDto);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + userProfileAndTokenDto.getToken());

        return ResponseEntity.ok()
                .headers(headers)
                .body(userProfileAndTokenDto.getUserProfileDto());
    }

    @Operation(summary = "Аутентификация.")
    @PostMapping("/login")
    public ResponseEntity<UserProfileDto> userSignIn(@RequestBody @Valid UserSignInDto userSignInDto) {
        UserProfileAndTokenDto userProfileAndTokenDto = userService.userSignIn(userSignInDto);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + userProfileAndTokenDto.getToken());

        return ResponseEntity.ok()
                .headers(headers)
                .body(userProfileAndTokenDto.getUserProfileDto());
    }

    @Operation(
            summary = "Просмотр данных профиля аутентифицированного пользователя.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getUserProfileInfo() {
        return new ResponseEntity<>(userService.getUserProfileInfo(), HttpStatus.OK);
    }

    @Operation(
            summary = "Изменение профиля.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping()
    public ResponseEntity<UserProfileDto> updateUserInfo(@RequestBody @Valid UserUpdateInfoDto userUpdateInfoDto) {
        return new ResponseEntity<>(userService.updateUserInfo(userUpdateInfoDto), HttpStatus.OK);
    }
}
