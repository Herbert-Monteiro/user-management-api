package com.herbertmonteiro.usermanagementapi.controller;

import com.herbertmonteiro.usermanagementapi.dto.LoanResponseDTO;
import com.herbertmonteiro.usermanagementapi.dto.UserRequestDTO;
import com.herbertmonteiro.usermanagementapi.dto.UserResponseDTO;
import com.herbertmonteiro.usermanagementapi.dto.UserUpdateDTO;
import com.herbertmonteiro.usermanagementapi.service.LoanService;
import com.herbertmonteiro.usermanagementapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminController {

    private final UserService userService;
    private final LoanService loanService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserRequestDTO dto){
        return new ResponseEntity<>(userService.createUser(dto),HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id){
                userService.deleteUser(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> ListUser(
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "10")int size){
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(userService.theWholeList(pageable),HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(
            @PathVariable("id") Long id,
            @RequestBody UserUpdateDTO dto){
                return new ResponseEntity<>(userService.updateUser(id,dto),HttpStatus.OK);

            }

    @GetMapping("/loans/active")
    public ResponseEntity<Page<LoanResponseDTO>> allActive(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(loanService.AllActive(pageable));
    }
    @GetMapping("/loans/history")
    public ResponseEntity<Page<LoanResponseDTO>> allHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10")int size){
        Pageable pageable= PageRequest.of(page, size);
        return ResponseEntity.ok(loanService.AllHistory(pageable));
    }

}
