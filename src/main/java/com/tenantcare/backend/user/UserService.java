package com.tenantcare.backend.user;

import com.tenantcare.backend.user.dto.UserRegistrationRequest;
import com.tenantcare.backend.user.dto.UserResponse;
import com.tenantcare.backend.user.exception.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse registerUser(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("Email is already taken");
        }

        User user = User.builder()
                .email(request.email())
                .password(request.password()) // TODO: This will be hashed later
                .firstName(request.firstName())
                .lastName(request.lastName())
                .role(Role.valueOf(request.role().toUpperCase()))
                .build();

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getRole()
        );
    }
}