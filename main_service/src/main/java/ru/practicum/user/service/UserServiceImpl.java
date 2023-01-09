package ru.practicum.user.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAll(List<Long> ids, Pageable pageable) {
        if (ids.isEmpty()) {
            return userRepository.findAll(pageable)
                    .stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        }

        return userRepository.findAllByIdIn(ids, pageable)
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public UserDto create(UserDto userDto) {
        if (userRepository.findByName(userDto.getName()).isPresent()) {
            throw new ConflictException("this name is already occupied");
        }
        User newUser = userRepository.save(UserMapper.toUser(userDto));

        return UserMapper.toUserDto(newUser);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id = " + id + " not found"));
        userRepository.delete(user);
    }
}
