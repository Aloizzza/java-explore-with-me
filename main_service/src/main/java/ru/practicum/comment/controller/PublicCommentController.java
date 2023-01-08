package ru.practicum.comment.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.service.CommentService;

import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/events/{eventId}/comments")
@AllArgsConstructor
public class PublicCommentController {
    private final CommentService commentService;

    @GetMapping
    public List<CommentDto> getAllByEvent(@Positive @PathVariable Long eventId,
                                          @RequestParam(defaultValue = "0") int from,
                                          @RequestParam(defaultValue = "10") int size) {
        log.info("get all comments for event with id {}", eventId);

        return commentService.getAllByEvent(eventId, PageRequest.of(from / size, size));
    }
}
