package ru.hits.musicservice.controller;

import io.minio.errors.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hits.musicservice.dto.*;
import ru.hits.musicservice.service.UserService;

import javax.validation.Valid;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

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
            summary = "Изменение данных профиля.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping
    public ResponseEntity<UserProfileDto> updateUserInfo(@RequestBody @Valid UserUpdateInfoDto userUpdateInfoDto) {
        return new ResponseEntity<>(userService.updateUserInfo(userUpdateInfoDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Изменение картинки заголовка.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping(value = "/upload-header-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserProfileDto> uploadHeaderImage(@RequestParam("file") MultipartFile headerImage) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return new ResponseEntity<>(userService.uploadHeaderImage(headerImage), HttpStatus.OK);
    }

    @Operation(
            summary = "Получить загруженные пользователем треки.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/{userId}/uploaded-tracks")
    public ResponseEntity<List<IncompleteTrackInfoDto>> getUploadedTracks(@PathVariable("userId") UUID userId) {
        return new ResponseEntity<>(userService.getUploadedTracks(userId), HttpStatus.OK);
    }

}
