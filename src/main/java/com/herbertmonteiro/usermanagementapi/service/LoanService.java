package com.herbertmonteiro.usermanagementapi.service;

import com.herbertmonteiro.usermanagementapi.dto.LoanRequestDTO;
import com.herbertmonteiro.usermanagementapi.dto.LoanResponseDTO;
import com.herbertmonteiro.usermanagementapi.entity.Book;
import com.herbertmonteiro.usermanagementapi.entity.Loan;
import com.herbertmonteiro.usermanagementapi.entity.User;
import com.herbertmonteiro.usermanagementapi.exeception.BookUnavailableException;
import com.herbertmonteiro.usermanagementapi.exeception.ResourceNotFoundException;
import com.herbertmonteiro.usermanagementapi.repository.BookRepository;
import com.herbertmonteiro.usermanagementapi.repository.LoanRepository;
import com.herbertmonteiro.usermanagementapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    private static final BigDecimal RATE_PHASE1 = BigDecimal.ONE;
    private static final BigDecimal RATE_PHASE2 = new BigDecimal("0.75");
    private static final BigDecimal RATE_PHASE3 = new BigDecimal("0.50");
    private static final BigDecimal FINE_RATE = new BigDecimal("0.01");

    public LoanResponseDTO create(LoanRequestDTO dto){
        var user = findLoggedUser();
        var book = findBook(dto.bookid());

        if (book.getQuantity() <= 0) {
            throw new BookUnavailableException("Book out of stock");
        }
        if(loanRepository.existsByBookIdAndReturnDateIsNull(dto.bookid())){
            throw new BookUnavailableException("Book already on loan");
        }
        var loan = Loan.builder()
                .user(user)
                .book(book)
                .loanDate(LocalDate.now())
                .loanDays(dto.loanDays())
                .dailyRate(dto.dailyRate())
                .build();
        book.setQuantity(book.getQuantity() - 1);
        bookRepository.save(book);

        loanRepository.save(loan);
        return toResponse(loan);
    }
    private User findLoggedUser(){
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));
    }

    public LoanResponseDTO returnBook(Long loanId){
        var loan = findLoan(loanId);

        loan.setReturnDate(LocalDate.now());

        var book = loan.getBook();
        book.setQuantity(book.getQuantity() + 1);
        bookRepository.save(book);

        loanRepository.save(loan);
        return toResponse(loan);
    }

    public List<LoanResponseDTO> listActive(){
        return loanRepository.findByReturnDateIsNull()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<LoanResponseDTO> myHistory(){
        var user = findLoggedUser();
        return loanRepository.findByUserId(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private Book findBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
    }

    private Loan findLoan(Long id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));
    }

    private BigDecimal calculateTotalPrice(Integer loanDays, BigDecimal dailyRate){
        BigDecimal total = BigDecimal.ZERO;

        for (int day = 1; day <= loanDays; day++){
            BigDecimal multiplier;

            if (day <=3) {
                multiplier = RATE_PHASE1;
            } else if (day <= 6) {
                multiplier = RATE_PHASE2;
            }else {
                multiplier = RATE_PHASE3;
            }
            total = total.add(dailyRate.multiply(multiplier));
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateFine(BigDecimal totalPrice, long daysLate) {
        BigDecimal factor = BigDecimal.ONE.add(FINE_RATE).pow((int) daysLate);

        return totalPrice.multiply(factor).setScale(2, RoundingMode.HALF_UP);
    }

    public Page<LoanResponseDTO> AllHistory(Pageable pageable){
        return loanRepository.findAll(pageable)
                .map(this::toResponse);
    }

    public Page<LoanResponseDTO> AllActive(Pageable pageable){
        return loanRepository.findByReturnDateIsNull(pageable)
                .map(this::toResponse);
    }
    private LoanResponseDTO toResponse(Loan loan) {
        BigDecimal totalPrice = calculateTotalPrice(loan.getLoanDays(), loan.getDailyRate());

        LocalDate deadline = loan.getLoanDate().plusDays(loan.getLoanDays());
        LocalDate referenceDate = loan.getReturnDate() != null
                ? loan.getReturnDate()
                : LocalDate.now();
       long dayLate = ChronoUnit.DAYS.between(deadline, referenceDate);

       BigDecimal totalWithFine = dayLate > 0
               ? calculateFine(totalPrice,dayLate)
               : totalPrice;

        return new LoanResponseDTO(
                loan.getId(),
                loan.getUser().getId(),
                loan.getUser().getName(),
                loan.getBook().getId(),
                loan.getBook().getTitle(),
                loan.getLoanDate(),
                loan.getReturnDate(),
                loan.getLoanDays(),
                loan.getDailyRate(),
                totalPrice,
                totalWithFine
        );
    }
}
