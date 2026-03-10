package com.example.accountservice.mapper;

import com.example.accountservice.dto.UserRequestDTO;
import com.example.accountservice.dto.UserResponseDTO;
import com.example.accountservice.entity.User;

public class UserMapper {

    public static User toEntity(UserRequestDTO dto){

        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    public static UserResponseDTO toResponse(User user){

        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
