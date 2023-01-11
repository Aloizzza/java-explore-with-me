package ru.practicum.event.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @NotBlank
    private String annotation;

    private Long category;

    @NotBlank
    private String description;

    @NotBlank
    private String eventDate;

    @NotNull
    private LocationDto location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    @NotBlank
    private String title;
}