package ru.practicum.user.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAll(List<Long> ids, PageRequest pageRequest);

    UserDto create(UserDto userDto);

    void delete(Long id);
}
