package com.herbertmonteiro.usermanagementapi.controller;

import com.herbertmonteiro.usermanagementapi.dto.LoanRequestDTO;
import com.herbertmonteiro.usermanagementapi.dto.LoanResponseDTO;
import com.herbertmonteiro.usermanagementapi.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
@RequiredArgsConstructor
public class LoanController {
    private final LoanService loanService;

    @PostMapping
    public ResponseEntity<LoanResponseDTO> create(@Valid @RequestBody LoanRequestDTO dto) {
        return ResponseEntity.status(201).body(loanService.create(dto));
    }
    @PutMapping("/{id}/return")
    public ResponseEntity<LoanResponseDTO> returnBook(@PathVariable Long id){
        return ResponseEntity.ok(loanService.returnBook(id));
    }
    @GetMapping("/active")
    public ResponseEntity<List<LoanResponseDTO>> listActive(){
        return ResponseEntity.ok(loanService.listActive());
    }
}
