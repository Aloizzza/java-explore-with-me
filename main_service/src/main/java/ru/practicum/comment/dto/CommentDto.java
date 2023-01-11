package ru.practicum.comment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
public class CommentDto {
    private Long id;

    @NotBlank
    private String text;

    private String authorName;

    private String createdOn;
}