package com.herbertmonteiro.usermanagementapi.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import com.herbertmonteiro.usermanagementapi.enums.Role;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false , length = 11, unique = true)
    private String cpf;

    @Column(nullable = false , length = 100 , unique = true)
    private String email;

    @Column(nullable = false ,length = 64)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private  LocalDateTime created_At;

    @Column(nullable = false)
    private LocalDate birth_Date;
}
