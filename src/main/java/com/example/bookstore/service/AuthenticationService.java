package com.example.bookstore.service;


import com.example.bookstore.enums.RoleName;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.exception.InvalidTokenException;
import com.example.bookstore.exception.ResourceAlreadyUsedException;
import com.example.bookstore.persistance.entity.Role;
import com.example.bookstore.persistance.entity.User;
import com.example.bookstore.persistance.repository.RoleRepository;
import com.example.bookstore.persistance.repository.UserRepository;
import com.example.bookstore.security.CustomUserDetailsService;
import com.example.bookstore.security.dto.RefreshTokenRequestDTO;
import com.example.bookstore.service.dto.UserRegistrationDTO;
import com.example.bookstore.service.dto.UserRegistrationResponseDTO;
import lombok.RequiredArgsConstructor;
import com.example.bookstore.security.dto.LoginRequestDTO;
import com.example.bookstore.security.dto.LoginResponseDTO;
import com.example.bookstore.security.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    public LoginResponseDTO authenticate(LoginRequestDTO loginRequest) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        final String accessToken = jwtUtil.generateAccessToken(userDetails);
        final String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        return LoginResponseDTO.builder()
                .withUsername(userDetails.getUsername())
                .withAccessToken(accessToken)
                .withRefreshToken(refreshToken)
                .build();
    }

    public LoginResponseDTO refresh(RefreshTokenRequestDTO refreshTokenRequestDTO) {
        String existingRefreshToken = refreshTokenRequestDTO.getRefreshToken();
        String username = jwtUtil.getUsername(existingRefreshToken);

        if (!jwtUtil.isVerified(existingRefreshToken)) {
            throw new InvalidTokenException("Invalid refresh token");
        }

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        String newAccessToken = jwtUtil.generateAccessToken(userDetails);
        return LoginResponseDTO.builder().
                withUsername(username).
                withAccessToken(newAccessToken)
                .withRefreshToken(existingRefreshToken)
                .build();
    }

    public UserRegistrationResponseDTO register( UserRegistrationDTO userRegistrationDTO) {
        if (userRepository.existsByEmail(userRegistrationDTO.getEmail())) {
            throw new ResourceAlreadyUsedException("User with this email already exists");
        }

        final Role role = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));

        final User user = new User();
        user.setFirstname(userRegistrationDTO.getFirstname());
        user.setLastname(userRegistrationDTO.getLastname());
        user.setEmail(userRegistrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        user.setEnabled(true);
        user.setRole(role);
        user.setEnabled(true);
        userRepository.save(user);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());
        final String accessToken = jwtUtil.generateAccessToken(userDetails);
        final String refreshToken = jwtUtil.generateRefreshToken(userDetails);
        UserRegistrationResponseDTO userRegistrationResponseDTO = UserRegistrationResponseDTO.toDTO(user);
        userRegistrationResponseDTO.setAccessToken(accessToken);
        userRegistrationResponseDTO.setRefreshToken(refreshToken);
        return userRegistrationResponseDTO;
    }
}
