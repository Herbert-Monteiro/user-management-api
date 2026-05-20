package com.herbertmonteiro.usermanagementapi.controller;

import com.herbertmonteiro.usermanagementapi.dto.BookRequestDTO;
import com.herbertmonteiro.usermanagementapi.dto.BookResponseDTO;
import com.herbertmonteiro.usermanagementapi.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;



@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/admin/books")
    public ResponseEntity<BookResponseDTO> create(@Valid @RequestBody BookRequestDTO dto){
        return ResponseEntity.ok(bookService.create(dto));
    }

    @DeleteMapping("/admin/books/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id){
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/books")
    public ResponseEntity<Page<BookResponseDTO>> list(
            @RequestParam(required = false)String search,
            @RequestParam(defaultValue= "0")int page,
            @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page,size);
        return ResponseEntity.ok(bookService.list(search,pageable));
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<BookResponseDTO>findById(@PathVariable Long id){
        return ResponseEntity.ok(bookService.findById(id));
    }
}
