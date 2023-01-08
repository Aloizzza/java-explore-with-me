package ru.practicum.comment.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/comments")
@AllArgsConstructor
public class UserCommentController {
    private final CommentService commentService;

    @GetMapping
    public List<CommentDto> getAllByUser(@Positive @PathVariable Long userId,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size) {
        log.info("get all comments for user with id {}", userId);
        return commentService.getAllByUser(userId, PageRequest.of(from / size, size));
    }

    @PostMapping("/{eventId}")
    public CommentDto create(@Valid @RequestBody CommentDto commentDto,
                             @Positive @PathVariable Long userId,
                             @Positive @PathVariable Long eventId) {
        log.info("create comment by user with id {}", userId);

        return commentService.create(commentDto, userId, eventId);
    }

    @PatchMapping("/{commentId}")
    public CommentDto update(@Positive @PathVariable Long commentId,
                             @Positive @PathVariable Long userId,
                             @Valid @RequestBody CommentDto commentDto) {
        log.info("update comment with id {}", commentId);

        return commentService.update(commentId, userId, commentDto);
    }

    @DeleteMapping("/{commentId}")
    public void delete(@Positive @PathVariable Long userId,
                       @Positive @PathVariable Long commentId) {
        log.info("delete comment with id {}", commentId);
        commentService.delete(commentId, userId);
    }
}
