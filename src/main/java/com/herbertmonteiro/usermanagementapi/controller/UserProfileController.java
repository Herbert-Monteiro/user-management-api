package com.herbertmonteiro.usermanagementapi.controller;
import com.herbertmonteiro.usermanagementapi.dto.LoanResponseDTO;
import com.herbertmonteiro.usermanagementapi.dto.UserResponseDTO;
import com.herbertmonteiro.usermanagementapi.dto.UserUpdateDTO;
import com.herbertmonteiro.usermanagementapi.service.LoanService;
import com.herbertmonteiro.usermanagementapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;



@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserProfileController {

    private final UserService userService;
    private final LoanService loanService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO>getMe(){
        return ResponseEntity.ok(userService.getLoggedUser());
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO>updateMe(@RequestBody UserUpdateDTO dto){
        return ResponseEntity.ok(userService.updateLoggedUser(dto));
    }

    @GetMapping("/me/history")
    public ResponseEntity<List<LoanResponseDTO>> myHistory(){
        return ResponseEntity.ok(loanService.myHistory());
    }
}
