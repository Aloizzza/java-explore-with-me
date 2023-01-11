package ru.practicum.comment.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.service.CommentService;

import javax.validation.constraints.Positive;

@Slf4j
@RestController
@RequestMapping("/admin/comments")
@AllArgsConstructor
public class AdminCommentController {
    private final CommentService commentService;

    @PatchMapping("/{commentId}/approve")
    public CommentDto approve(@Positive @PathVariable Long commentId) {
        log.info("approve comment {}", commentId);

        return commentService.approve(commentId);
    }

    @PatchMapping("/{commentId}/reject")
    public CommentDto reject(@Positive @PathVariable Long commentId) {
        log.info("reject comment {}", commentId);

        return commentService.reject(commentId);
    }
}
