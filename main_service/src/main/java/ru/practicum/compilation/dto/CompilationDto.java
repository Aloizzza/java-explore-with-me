package ru.practicum.compilation.dto;

import lombok.*;
import ru.practicum.event.dto.ShortEventDto;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {
    private Long id;

    @NotBlank
    private String title;

    private Boolean pinned;

    private List<ShortEventDto> events;
}