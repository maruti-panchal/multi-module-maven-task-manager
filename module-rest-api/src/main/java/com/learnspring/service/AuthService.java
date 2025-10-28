package com.learnspring.service;


import com.learnspring.dtos.LoginRequestDto;
import com.learnspring.dtos.LoginResponseDto;
import com.learnspring.dtos.SignUpRequestDto;
import com.learnspring.dtos.SignupResponseDto;
import com.learnspring.entity.UserEntity;
import com.learnspring.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    public AuthService(PasswordEncoder passwordEncoder, UserRepository userRepository, AuthenticationService authenticationService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
    }

    public Mono<SignupResponseDto> signup(Mono<SignUpRequestDto> signUpRequestDto) {
        return signUpRequestDto
                .map(this::convertSignUpRequestDtoToUserEntity)
                .flatMap(userRepository::save)
                .map(this::convertUserEntityToSignUpResponseDto);
    }

    public Mono<LoginResponseDto> login(Mono<LoginRequestDto> loginRequestDto) {
        return loginRequestDto.flatMap(loginRequest->
                authenticationService
                        .authenticate(loginRequest.getUsername(),loginRequest.getPassword())
                        .map(auth->LoginResponseDto.builder().token(auth.get("Token")).username(loginRequest.getUsername()).build())
        );
    }

    private UserEntity convertSignUpRequestDtoToUserEntity(SignUpRequestDto signUpRequestDto){
        return UserEntity.builder()
                .firstName(signUpRequestDto.getFirstName())
                .lastName(signUpRequestDto.getLastName())
                .email(signUpRequestDto.getEmail())
                .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                .roles(List.of(signUpRequestDto.getRole()))
                .username(signUpRequestDto.getUsername())
                .phone(signUpRequestDto.getPhone())
                .build();
    }

    private SignupResponseDto convertUserEntityToSignUpResponseDto(UserEntity userEntity){
        return SignupResponseDto.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .phone(userEntity.getPhone())
                .role(userEntity.getRoles().toString())
                .build();
    }
}
