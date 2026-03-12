package com.example.accountservice.controller;

import com.example.accountservice.dto.UserPatchDTO;
import com.example.accountservice.dto.UserRequestDTO;
import com.example.accountservice.dto.UserResponseDTO;
import com.example.accountservice.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void CreateUser() throws Exception {

        UserRequestDTO request = new UserRequestDTO("John", "john@email.com");

        UserResponseDTO response = new UserResponseDTO(1L, "John", "john@email.com");

        when(userService.createUser(Mockito.any())).thenReturn(response);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"));
    }
    @Test
    void GetUsers() throws Exception {

        UserResponseDTO user = new UserResponseDTO(1L, "John", "john@email.com");

        when(userService.getUsers(0, 5, "name", "asc"))
                .thenReturn(List.of(user));

        mockMvc.perform(get("/api/users")
                        .param("page", "0")
                        .param("size", "5")
                        .param("sortBy", "name")
                        .param("direction", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John"));
    }
    @Test
    void GetUser() throws Exception {

        UserResponseDTO response = new UserResponseDTO(1L, "John", "john@email.com");

        when(userService.getUser(1L)).thenReturn(response);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"));
    }
    @Test
    void DeleteUser() throws Exception {

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());

        Mockito.verify(userService).deleteUser(1L);
    }
    @Test
    void PatchUser() throws Exception {

        UserPatchDTO patchDTO = UserPatchDTO.builder().name("UpdatedName").build();

        UserResponseDTO response = new UserResponseDTO(1L, "UpdatedName", "john@email.com");

        when(userService.patchUser(Mockito.eq(1L), Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(patch("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UpdatedName"));
    }
    @Test
    void CreateUsersBulk() throws Exception {

        UserRequestDTO request = new UserRequestDTO("John", "john@email.com");

        UserResponseDTO response = new UserResponseDTO(1L, "John", "john@email.com");

        when(userService.createMultiUsers(Mockito.any()))
                .thenReturn(List.of(response));

        mockMvc.perform(post("/api/users/bulk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(request))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John"));
    }
}