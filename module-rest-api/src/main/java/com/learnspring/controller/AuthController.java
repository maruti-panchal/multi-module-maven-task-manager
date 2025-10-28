package com.learnspring.controller;



import com.learnspring.dtos.LoginRequestDto;
import com.learnspring.dtos.LoginResponseDto;
import com.learnspring.dtos.SignUpRequestDto;
import com.learnspring.dtos.SignupResponseDto;
import com.learnspring.service.AuthService;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public Mono<ResponseEntity<SignupResponseDto>> signup(@RequestBody Mono<SignUpRequestDto> signUpRequestDto){
        return authService.signup(signUpRequestDto).map(ResponseEntity::ok);
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<LoginResponseDto>> login(@RequestBody Mono<LoginRequestDto> loginRequestDto) {
        return authService.login(loginRequestDto).map(ResponseEntity::ok);
    }

}
