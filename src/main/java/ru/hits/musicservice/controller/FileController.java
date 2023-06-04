package ru.hits.musicservice.controller;

import io.minio.errors.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hits.musicservice.dto.FileDownloadDto;
import ru.hits.musicservice.service.FileService;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Tag(name = "Работа с файлами.")
public class FileController {

    private final FileService fileService;

    @Operation(summary = "Загрузка файла.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String upload(@RequestParam("file") MultipartFile file) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return fileService.upload(file);
    }

    @Operation(summary = "Получение файла по id.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/download/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> download(@PathVariable("id") UUID id) {
        FileDownloadDto fileDownloadDto = fileService.download(id);
        return ResponseEntity.ok()
                .header("Content-Type", MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .header("Content-Disposition", "filename=" + fileDownloadDto.getFilename())
                .body(fileDownloadDto.getIn());
    }

}
