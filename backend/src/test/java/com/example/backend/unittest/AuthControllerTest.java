package com.example.backend.unittest;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.backend.controllers.AuthController;
import com.example.backend.entities.Account;
import com.example.backend.entities.Role;
import com.example.backend.entities.UserEntity;
import com.example.backend.enums.AccountStatus;
import com.example.backend.enums.ERole;
import com.example.backend.exceptions.InsufficientBalanceException;
import com.example.backend.repositories.RoleRepository;
import com.example.backend.repositories.UserRepository;
import com.example.backend.security.jwt.JwtUtils;
import com.example.backend.security.payload.request.LoginRequest;
import com.example.backend.security.payload.request.SignupRequest;
import com.example.backend.security.payload.response.JwtResponse;
import com.example.backend.security.payload.response.SignupResponse;
import com.example.backend.security.service.UserDetailsImpl;
import com.example.backend.services.AccountService;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private AccountService accountService;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticateUser_success() {
        LoginRequest loginRequest = new LoginRequest("username", "password");

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("mock-jwt-token");
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("username");
        when(userDetails.getId()).thenReturn(1L);
        when(userDetails.getAuthorities()).thenReturn(new HashSet<>());

        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof JwtResponse);
        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertEquals("mock-jwt-token", jwtResponse.getToken());
        assertEquals("username", jwtResponse.getUsername());
    }

    @Test
    void testAuthenticateUser_invalidCredentials() {
        LoginRequest loginRequest = new LoginRequest("invaliduser", "invalidpassword");

        when(authenticationManager.authenticate(any())).thenThrow(new RuntimeException("Authentication failed"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authController.authenticateUser(loginRequest));
        assertEquals("Authentication failed", exception.getMessage());
    }

    @Test
    void testRegisterUser_success() {
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setUsername("newuser");
        signUpRequest.setPassword("password123");
        signUpRequest.setHolderName("John Doe");
        signUpRequest.setMinBalance(1500.0);
        Set<String> roles = new HashSet<>();
        roles.add("user");
        signUpRequest.setRole(roles);

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(roleRepository.findByRoleName(ERole.ROLE_USER)).thenReturn(Optional.of(new Role(ERole.ROLE_USER)));
        when(accountService.generateAccountId()).thenReturn("123456789");

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("newuser");
        user.setPassword("encodedpassword");
        user.setRoles(new HashSet<>());
        Account account = new Account();
        account.setAccountId("123456789");
        account.setHolderName("John Doe");
        account.setBalance(1500.0);
        account.setStatus(AccountStatus.ACTIVE);
        user.setAccount(account);

        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        ResponseEntity<?> response = authController.registerUser(signUpRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof SignupResponse);
        SignupResponse signupResponse = (SignupResponse) response.getBody();
        assertEquals("newuser", signupResponse.getUsername());
        assertEquals("John Doe", signupResponse.getHolderName());
        assertEquals(1500.0, signupResponse.getBalance());
    }

    @Test
    void testRegisterUser_usernameAlreadyTaken() {
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setUsername("existinguser");
        signUpRequest.setPassword("password123");
        signUpRequest.setHolderName("Jane Doe");
        signUpRequest.setMinBalance(1500.0);

        when(userRepository.existsByUsername("existinguser")).thenReturn(true);

        ResponseEntity<?> response = authController.registerUser(signUpRequest);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testRegisterUser_roleNotFound() {
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setUsername("newuser");
        signUpRequest.setPassword("password123");
        signUpRequest.setHolderName("John Doe");
        signUpRequest.setMinBalance(1500.0);
        Set<String> roles = new HashSet<>();
        roles.add("nonexistentrole");
        signUpRequest.setRole(roles);

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(roleRepository.findByRoleName(any())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authController.registerUser(signUpRequest));
        assertEquals("Error: Role is not found.", exception.getMessage());
    }

    @Test
    void testRegisterUser_insufficientBalance() {
        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setUsername("lowbalanceuser");
        signUpRequest.setPassword("password123");
        signUpRequest.setHolderName("Low Balance");
        signUpRequest.setMinBalance(500.0);

        when(userRepository.existsByUsername("lowbalanceuser")).thenReturn(false);

        assertThrows(InsufficientBalanceException.class,
                () -> authController.registerUser(signUpRequest));
    }
}
