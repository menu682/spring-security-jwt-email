package ua.ldv.server.controller.security;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.ldv.server.dto.MessageResponseDto;
import ua.ldv.server.dto.security.JwtResponseDto;
import ua.ldv.server.dto.security.LoginRequestDto;
import ua.ldv.server.dto.security.SignupRequestDto;
import ua.ldv.server.service.security.AuthService;

import jakarta.validation.Valid;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController()
@RequestMapping("/api/auth/")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public JwtResponseDto login(@Valid @RequestBody LoginRequestDto loginRequestDTO){
        return authService.loginUser(loginRequestDTO);
    }

    @PostMapping("/register")
    public MessageResponseDto register(@Valid @RequestBody SignupRequestDto signupRequestDTO){
        return authService.registerUser(signupRequestDTO);
    }

}