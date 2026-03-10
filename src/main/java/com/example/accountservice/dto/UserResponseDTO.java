package com.example.accountservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@Builder
public class UserResponseDTO {

    private Long id;

    private String name;

    private String email;
}
