package ru.practicum.comment.mapper;

import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.model.Comment;

import java.time.LocalDateTime;

import static ru.practicum.comment.model.CommentState.NEW;
import static ru.practicum.utility.Constant.DATE_TIME_FORMAT;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto
                .builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getUser().getName())
                .createdOn(comment.getCreatedOn()
                        .format(DATE_TIME_FORMAT))
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
