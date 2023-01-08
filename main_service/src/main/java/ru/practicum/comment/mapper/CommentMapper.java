package ru.practicum.comment.mapper;

import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.model.Comment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.practicum.comment.model.CommentState.NEW;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto
                .builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getUser().getName())
                .createdOn(comment.getCreatedOn()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    public static Comment toComment(CommentDto commentDto) {
        return Comment
                .builder()
                .text(commentDto.getText())
                .createdOn(LocalDateTime.now())
                .state(NEW)
                .build();
    }
}
