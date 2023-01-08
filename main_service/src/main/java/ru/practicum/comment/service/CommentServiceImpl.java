package ru.practicum.comment.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.comment.model.CommentState.*;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public List<CommentDto> getAllByEvent(Long eventId, Pageable pageable) {
        Event event = checkAndGetEvent(eventId);

        return commentRepository.findAllByEventAndState(event, APPROVED, pageable)
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getAllByUser(Long userId, Pageable pageable) {
        User user = checkAndGetUser(userId);
        
        return commentRepository.findAllByUser(user, pageable)
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto create(CommentDto commentDto, Long userId, Long eventId) {
        User user = checkAndGetUser(userId);
        Event event = checkAndGetEvent(eventId);
        Comment comment = CommentMapper.toComment(commentDto);
        comment.setUser(user);
        comment.setEvent(event);
        Comment newComment = commentRepository.save(comment);

        return CommentMapper.toCommentDto(newComment);
    }

    @Override
    @Transactional
    public CommentDto update(Long commentId, Long userId, CommentDto commentDto) {
        Comment comment = commentRepository.findByIdAndUserId(commentId, userId)
                .orElseThrow(() -> new BadRequestException("only author can change comment"));
        comment.setText(commentDto.getText());
        comment.setState(NEW);
        Comment updatedComment = commentRepository.save(comment);

        return CommentMapper.toCommentDto(updatedComment);
    }

    @Override
    @Transactional
    public void delete(Long commentId, Long userId) {
        Comment comment = commentRepository.findByIdAndUserId(commentId, userId)
                .orElseThrow(() -> new BadRequestException("only author can delete comment"));
        commentRepository.delete(comment);
    }

    @Override
    @Transactional
    public CommentDto approve(Long commentId) {
        Comment comment = checkAndGetComment(commentId);
        comment.setState(APPROVED);
        Comment approvedComment = commentRepository.save(comment);

        return CommentMapper.toCommentDto(approvedComment);
    }

    @Override
    @Transactional
    public CommentDto reject(Long commentId) {
        Comment comment = checkAndGetComment(commentId);
        comment.setState(REJECTED);
        Comment rejectedComment = commentRepository.save(comment);

        return CommentMapper.toCommentDto(rejectedComment);
    }

    private Event checkAndGetEvent(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("event with id = " + id + " not found"));
    }

    private User checkAndGetUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("user with id = " + id + " not found"));
    }

    private Comment checkAndGetComment(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("comment with id = " + id + " not found"));
    }
}
