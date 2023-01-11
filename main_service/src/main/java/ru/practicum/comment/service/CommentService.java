package ru.practicum.comment.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.comment.dto.CommentDto;

import java.util.List;

public interface CommentService {
    List<CommentDto> getAllByEvent(Long eventId, Pageable pageable);

    List<CommentDto> getAllByUser(Long userId, Pageable pageable);

    CommentDto create(CommentDto commentDto, Long userId, Long eventId);

    CommentDto update(Long commentId, Long userId, CommentDto commentDto);

    void delete(Long commentId, Long userId);

    CommentDto approve(Long commentId);

    CommentDto reject(Long commentId);
}
