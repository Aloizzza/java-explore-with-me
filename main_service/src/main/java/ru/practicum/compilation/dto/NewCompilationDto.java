package ru.practicum.compilation.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {
    @NotBlank
    private String title;

    private Boolean pinned;

    private List<Long> events;
}
