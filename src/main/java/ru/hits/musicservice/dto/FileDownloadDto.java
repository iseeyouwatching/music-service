package ru.hits.musicservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FileDownloadDto {

    private byte[] in;

    private String filename;

}
