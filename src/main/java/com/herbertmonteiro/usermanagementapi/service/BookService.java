package com.herbertmonteiro.usermanagementapi.service;

import com.herbertmonteiro.usermanagementapi.entity.Book;
import com.herbertmonteiro.usermanagementapi.exeception.ResourceNotFoundException;
import com.herbertmonteiro.usermanagementapi.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.herbertmonteiro.usermanagementapi.dto.BookRequestDTO;
import com.herbertmonteiro.usermanagementapi.dto.BookResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public BookResponseDTO create(BookRequestDTO dto) {
        Book book = Book.builder()
                .title(dto.title())
                .author(dto.author())
                .isbn(dto.isbn())
                .quantity(dto.quantity())
                .createdAt(LocalDateTime.now())
                .build();

        return toResponse(bookRepository.save(book));
    }
    public Page<BookResponseDTO> list(String search, Pageable pageable){
        if(search != null && !search.isBlank()){
            return bookRepository
                    .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
                            search,search,pageable)
                    .map(this::toResponse);
        }

        return bookRepository.findAll(pageable).map(this::toResponse);

    }

    private Book findBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
    }
    public BookResponseDTO findById(Long id) {
        return toResponse(findBook(id));
    }

    public List<BookResponseDTO> list() {
        return bookRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();

    }

    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found");
        }
        bookRepository.deleteById(id);
    }

    private BookResponseDTO toResponse(Book book) {
        return new BookResponseDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getQuantity(),
                book.getCreatedAt()
        );
    }
}

