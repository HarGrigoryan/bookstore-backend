package com.example.bookstore.service;

import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.service.dto.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import com.example.bookstore.exception.ResourceAlreadyUsedException;
import com.example.bookstore.persistance.entity.Role;
import com.example.bookstore.persistance.entity.User;
import com.example.bookstore.persistance.repository.RoleRepository;
import com.example.bookstore.persistance.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Transactional
    public UserDTO createUser(UserCreateDTO userCreateDTO) {

        if (userRepository.existsByEmail(userCreateDTO.getEmail())) {
            throw new ResourceAlreadyUsedException("User with this email already exists");
        }

        final Role role = roleRepository.findByName(userCreateDTO.getRole())
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));

        final User user = new User();
        user.setFirstname(userCreateDTO.getFirstname());
        user.setLastname(userCreateDTO.getLastname());
        user.setEmail(userCreateDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userCreateDTO.getTemporaryPassword()));
        user.setEnabled(true);
        user.setRole(role);

        return UserDTO.toDTO(userRepository.save(user));
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDTO::toDTO)
                .toList();
    }

    public UserDTO getById(Long id) {

        final User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return UserDTO.toDTO(user);
    }

    @Transactional
    public UserDTO updateUser(Long id, UserUpdateDTO updateDto) {
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setFirstname(updateDto.getFirstname());
        user.setLastname(updateDto.getLastname());

        return UserDTO.toDTO(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        userRepository.deleteById(id);
    }

    public UserDTO changePassword(Long id,  UserPasswordChangeRequestDTO userPasswordChangeRequestDTO) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id '%s' not found".formatted(id)));
        user.setPassword(passwordEncoder.encode(userPasswordChangeRequestDTO.getNewPassword()));
        return UserDTO.toDTO(userRepository.save(user));
    }
}
