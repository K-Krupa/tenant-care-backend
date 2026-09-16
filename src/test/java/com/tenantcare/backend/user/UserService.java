package com.tenantcare.backend.user;

import com.tenantcare.backend.user.dto.UserRegistrationRequest;
import com.tenantcare.backend.user.dto.UserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser_ShouldReturnUserResponse_WhenEmailIsUnique() {
        // given
        UserRegistrationRequest request = new UserRegistrationRequest(
                "test@example.com", "password123", "Jan", "Kowalski", "LANDLORD"
        );

        User savedUser = User.builder()
                .id(1L)
                .email(request.email())
                .password(request.password())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .role(Role.LANDLORD)
                .build();

        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword123");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // when
        UserResponse response = userService.registerUser(request);

        // then
        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("test@example.com", response.email());
        assertEquals("Jan", response.firstName());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_ShouldThrowException_WhenEmailIsAlreadyTaken() {
        // given
        UserRegistrationRequest request = new UserRegistrationRequest(
                "taken@example.com", "password123", "Jan", "Kowalski", "LANDLORD"
        );

        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.registerUser(request);
        });

        assertEquals("Email is already taken", exception.getMessage());

        verify(userRepository, never()).save(any(User.class));
    }
}