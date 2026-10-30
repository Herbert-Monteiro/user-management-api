package com.herbertmonteiro.usermanagementapi.service;
import com.herbertmonteiro.usermanagementapi.dto.LoginRequestDTO;
import com.herbertmonteiro.usermanagementapi.dto.RegisterRequestDTO;
import com.herbertmonteiro.usermanagementapi.entity.User;
import com.herbertmonteiro.usermanagementapi.enums.Role;
import com.herbertmonteiro.usermanagementapi.exeception.ConflictException;
import com.herbertmonteiro.usermanagementapi.exeception.InvalidDataException;
import com.herbertmonteiro.usermanagementapi.exeception.UnauthorizedException;
import com.herbertmonteiro.usermanagementapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JWTService jwtService;

    public void register(RegisterRequestDTO dto) {

        String cleanCpf = dto.cpf().replaceAll("[^0-9]", "");
        if (cleanCpf.length() != 11) {
            throw new InvalidDataException("CPF invalid");
        }

        if (userRepository.existsByCpf(cleanCpf)){
            throw new ConflictException("CPF already registered");
        }

        if (userRepository.existsByEmail(dto.email())) {
            throw new ConflictException("Email already registered");
        }

        User user = new User();
        user.setName(dto.nome());
        user.setCpf(cleanCpf);
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRole(Role.USER);
        user.setBirthDate(dto.birthDate());

        userRepository.save(user);
    }

    public String login(LoginRequestDTO dto){
        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(()-> new UnauthorizedException("Invalid email or password"));

        boolean passwordMatches = passwordEncoder.matches(
                dto.password(),
                user.getPassword()
        );
        if(!passwordMatches){
            throw new UnauthorizedException("Invalid email or password");
        }
        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        return token;
    }
}