package com.example.accountservice.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.example.accountservice.repository.UserRepository;
import com.example.accountservice.entity.User;

import com.example.accountservice.dto.UserRequestDTO;
import com.example.accountservice.dto.UserResponseDTO;
import com.example.accountservice.dto.UserPatchDTO;

import com.example.accountservice.mapper.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.accountservice.exception.UserNotFoundException;
import com.example.accountservice.exception.EmailAlreadyExistsException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDTO createUser(UserRequestDTO dto){

        if(userRepository.findByEmail(dto.getEmail()).isPresent()){
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User user = UserMapper.toEntity(dto);

        User saved = userRepository.save(user);

        return UserMapper.toResponse(saved);
    }

    public List<UserResponseDTO> createMultiUsers(List<UserRequestDTO> dtos){
        return dtos.stream()
                .map(this::createUser)
                .toList();
    }

    public List<UserResponseDTO> getUsers(Integer page, Integer size, String sortBy, String direction){
        if (page == null || size == null) {
            return userRepository.findAll().stream().map(UserMapper::toResponse).toList();
        }

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<User> userPage = userRepository.findAll(pageable);

        return userPage.getContent()
                .stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
    }

    public UserResponseDTO getUser(Long id){

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return UserMapper.toResponse(user);
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    public UserResponseDTO patchUser(Long id, UserPatchDTO dto){

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(dto.getName() != null){
            user.setName(dto.getName());
        }

        if(dto.getEmail() != null){

            if(userRepository.findByEmail(dto.getEmail()).isPresent()){
                throw new RuntimeException("Email already exists");
            }

            user.setEmail(dto.getEmail());
        }

        User updatedUser = userRepository.save(user);

        return UserMapper.toResponse(updatedUser);
    }
}
