package com.example.accountservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPatchDTO {

    private String name;

    private String email;
}
