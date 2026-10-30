package com.herbertmonteiro.usermanagementapi.service;
import com.herbertmonteiro.usermanagementapi.dto.UserRequestDTO;
import com.herbertmonteiro.usermanagementapi.dto.UserResponseDTO;
import com.herbertmonteiro.usermanagementapi.dto.UserUpdateDTO;
import com.herbertmonteiro.usermanagementapi.entity.User;
import com.herbertmonteiro.usermanagementapi.enums.Role;
import com.herbertmonteiro.usermanagementapi.exeception.ConflictException;
import com.herbertmonteiro.usermanagementapi.exeception.InvalidDataException;
import com.herbertmonteiro.usermanagementapi.exeception.ResourceNotFoundException;
import com.herbertmonteiro.usermanagementapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Locale;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO createUser(UserRequestDTO dto) {
        String cpf = cleanCpf(dto.cpf());
        checkDuplicates(dto.email(), cpf, null);

        User user = User.builder()
                .name(dto.name())
                .cpf(dto.cpf())
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .role(parseRole((dto.role())))
                .birthDate(dto.birthDate())
                .createdAt(LocalDateTime.now())
                .build();

        return toResponse(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    public Page<UserResponseDTO> theWholeList(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(this::toResponse);
    }

    public UserResponseDTO updateUser(Long id, UserUpdateDTO dto) {
        User user = findUserById(id);

        String cpf = dto.cpf() != null ? cleanCpf(dto.cpf()) : null;
        checkDuplicates(dto.email(), cpf, user.getId());

        if (dto.name() != null) user.setName(dto.name());
        if (dto.cpf() != null) user.setCpf(dto.cpf());
        if (dto.email() != null) user.setEmail(dto.email());
        if (dto.birthDate() != null) user.setBirthDate(dto.birthDate());
        if (dto.password() != null) user.setPassword(passwordEncoder.encode(dto.password()));

        return toResponse(userRepository.save(user));
    }

    public UserResponseDTO getLoggedUser() {
        return toResponse(findLoggedUser());
    }

    public UserResponseDTO updateLoggedUser(UserUpdateDTO dto) {
        User user = findLoggedUser();

        String cpf = dto.cpf() != null ? cleanCpf(dto.cpf()) : null;
        checkDuplicates(dto.email(), cpf, user.getId());

        if (dto.name() != null) user.setName(dto.name());
        if (dto.cpf() != null) user.setCpf(dto.cpf());
        if (dto.email() != null) user.setEmail(dto.email());
        if (dto.birthDate() != null) user.setBirthDate(dto.birthDate());

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
                user.getBirthDate(),
                user.getCreatedAt()
        );
    }

    private String cleanCpf(String cpf) {
        String clean = cpf.replaceAll("[^0-9]", "");
        if (clean.length() != 11) {
            throw new InvalidDataException("CPF invalid");
        }
        return clean;
    }

    private Role parseRole(String role) {
        if (role == null || role.isBlank())
            return Role.USER;

        try {
            return Role.valueOf(role.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidDataException("Invalid role. Allowed values: " + Arrays.toString(Role.values()));
        }
    }

    private void checkDuplicates(String email, String cpf, Long ignoreId) {
        if (email != null) {
            boolean taken = ignoreId == null
                    ? userRepository.existsByEmail(email)
                    : userRepository.existsByEmailAndIdNot(email, ignoreId);
            if (taken)
                throw new ConflictException("email already registered");
        }
        if (cpf != null) {
            boolean taken = ignoreId == null
                    ? userRepository.existsByCpf(cpf)
                    : userRepository.existsByCpfAndIdNot(cpf, ignoreId);
            if (taken)
                throw new ConflictException("cpf already registered");
        }
    }

}