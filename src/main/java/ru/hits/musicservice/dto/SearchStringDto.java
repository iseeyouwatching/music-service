package ru.hits.musicservice.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SearchStringDto {

    @NotBlank(message = "Поисковая строка не может быть пустой.")
    private String searchString;

}
