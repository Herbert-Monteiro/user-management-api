package com.herbertmonteiro.usermanagementapi.controller;
import com.herbertmonteiro.usermanagementapi.dto.LoginRequestDTO;
import com.herbertmonteiro.usermanagementapi.dto.LoginResponseDTO;
import com.herbertmonteiro.usermanagementapi.dto.RegisterRequestDTO;
import org.springframework.http.ResponseEntity;
import com.herbertmonteiro.usermanagementapi.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequestDTO request){
        authService.register(request);
        return ResponseEntity.status(201).body("User created");
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto){
        String token = authService.login(dto);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}
