package com.herbertmonteiro.usermanagementapi.service;
import com.herbertmonteiro.usermanagementapi.dto.UserRequestDTO;
import com.herbertmonteiro.usermanagementapi.dto.UserResponseDTO;
import com.herbertmonteiro.usermanagementapi.entity.User;
import com.herbertmonteiro.usermanagementapi.enums.Role;
import com.herbertmonteiro.usermanagementapi.exeception.ResourceNotFoundException;
import com.herbertmonteiro.usermanagementapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO createUser(UserRequestDTO dto){

        User user = User.builder()
                .name(dto.name())
                .cpf(dto.cpf())
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .role(dto.role() != null ? Role.valueOf(dto.role()) : Role.USER)
                .birth_Date(dto.birthDate())
                .created_At(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);

        return toResponse(savedUser);
    }

    public void deleteUser(Long id){
        if(!userRepository.existsById(id)){
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    public Page<UserResponseDTO> theWholeList(Pageable pageable){
        return userRepository.findAll(pageable)
                .map(this::toResponse);
    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {
        User user = findUserById(id);

        if (dto.name() != null)      user.setName(dto.name());
        if (dto.cpf() != null)       user.setCpf(dto.cpf());
        if (dto.email() != null)     user.setEmail(dto.email());
        if (dto.birthDate() != null) user.setBirth_Date(dto.birthDate());
        if (dto.password() != null)  user.setPassword(passwordEncoder.encode(dto.password()));

        return toResponse(userRepository.save(user));
    }

    public UserResponseDTO getLoggedUser() {
        return toResponse(findLoggedUser());
    }

    public UserResponseDTO updateLoggedUser(UserRequestDTO dto) {
        User user = findLoggedUser();


        if (dto.name() != null)      user.setName(dto.name());
        if (dto.cpf() != null)       user.setCpf(dto.cpf());
        if (dto.email() != null)     user.setEmail(dto.email());
        if (dto.birthDate() != null) user.setBirth_Date(dto.birthDate());

        return toResponse(userRepository.save(user));
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private User findLoggedUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private UserResponseDTO toResponse(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getCpf(),
                user.getEmail(),
                user.getRole().name(),
                user.getBirth_Date(),
                user.getCreated_At()
        );
    }
}