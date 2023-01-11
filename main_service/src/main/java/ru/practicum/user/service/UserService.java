package ru.practicum.user.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAll(List<Long> ids, Pageable pageable);

    UserDto create(UserDto userDto);

    void delete(Long id);
}
