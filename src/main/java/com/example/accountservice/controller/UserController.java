package com.example.accountservice.controller;

import lombok.RequiredArgsConstructor;

import java.util.List;

import com.example.accountservice.service.UserService;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;

import com.example.accountservice.dto.UserRequestDTO;
import com.example.accountservice.dto.UserResponseDTO;
import com.example.accountservice.dto.UserPatchDTO;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserResponseDTO createUser(
            @RequestBody UserRequestDTO dto){

        return userService.createUser(dto);
    }

    @GetMapping
    public List<UserResponseDTO> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction){

        return userService.getUsers(page, size, sortBy, direction);
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUser(
            @PathVariable Long id){

        return userService.getUser(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(
            @PathVariable Long id){

        userService.deleteUser(id);
    }

    @PatchMapping("/{id}")
    public UserResponseDTO patchUser(
            @PathVariable Long id,
            @RequestBody UserPatchDTO dto){

        return userService.patchUser(id, dto);
    }
}
