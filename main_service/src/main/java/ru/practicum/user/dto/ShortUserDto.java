package ru.practicum.user.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShortUserDto {
    private Long id;

    private String name;
}