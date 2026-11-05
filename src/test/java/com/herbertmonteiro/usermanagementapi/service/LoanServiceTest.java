package com.herbertmonteiro.usermanagementapi.service;

import com.herbertmonteiro.usermanagementapi.dto.LoanResponseDTO;
import com.herbertmonteiro.usermanagementapi.entity.Book;
import com.herbertmonteiro.usermanagementapi.entity.Loan;
import com.herbertmonteiro.usermanagementapi.entity.User;
import com.herbertmonteiro.usermanagementapi.repository.BookRepository;
import com.herbertmonteiro.usermanagementapi.repository.LoanRepository;
import com.herbertmonteiro.usermanagementapi.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private LoanService loanService;

    private static final BigDecimal DAILY_RATE = new BigDecimal("2.00");
    private static final LocalDate LOAN_DATE = LocalDate.of(2026,1,1);

    private Loan buildLoan(int loanDays, LocalDate localDate, LocalDate returnDate){
        User user = User.builder().id(1L).name("Herbert").build();
        Book book = Book.builder().id(10L).title("Clean Code").build();

        return Loan.builder()
                .id(100L)
                .user(user)
                .book(book)
                .loanDate(localDate)
                .returnDate(returnDate)
                .loanDays(loanDays)
                .dailyRate(DAILY_RATE)
                .build();
    }

    private LoanResponseDTO calculate(Loan loan) {
        Pageable pageable = PageRequest.of(0, 10);
        when(loanRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(loan), pageable, 1));

        return loanService.allHistory(pageable).getContent().get(0);
    }

    @ParameterizedTest(name = "{0} dias(s)  -> total {1}")
    @CsvSource({
            "1,  2.00",
            "3,  6.00",
            "4,  7.50",
            "6,  10.50",
            "7,  11.50",
            "10, 14.50"
    })

    @DisplayName("Preço total respeita as 3 fases de desconto")
    void shouldCalculateTotalPriceByPhases(int loanDays, String expectedTotal){
        Loan loan = buildLoan(loanDays, LOAN_DATE, LOAN_DATE.plusDays(loanDays));

        LoanResponseDTO result = calculate(loan);

        assertThat(result.totalPrice()).isEqualByComparingTo(expectedTotal);
        assertThat(result.totalWithFine()).isEqualByComparingTo(expectedTotal);
    }

    @Test
    @DisplayName("Devolvido eatamente no: sem multa")
    void  shouldNotApplyFineWhenReturnedEarly(){
        Loan loan = buildLoan(5, LOAN_DATE,LOAN_DATE.plusDays(2));

        LoanResponseDTO result = calculate(loan);

        assertThat(result.totalWithFine()).isEqualByComparingTo(result.totalPrice());
    }

    @ParameterizedTest(name = "{0} dia(s) de atraso -> {1}")
    @CsvSource({
            "1, 6.06",
            "10, 6.63",
            "30, 8.09"
    })
    @DisplayName("Multa é juros compostos de 1% ao dia sobre o preço total")
    void shouldApplyCompoundFine(Long daysLate, String expectedWithFine) {
        int loanDays = 3;
        LocalDate returnDate = LOAN_DATE.plusDays(loanDays + daysLate);
        Loan loan = buildLoan(loanDays, LOAN_DATE, returnDate);

        LoanResponseDTO result = calculate(loan);

        assertThat(result.totalPrice()).isEqualByComparingTo("6.00");
        assertThat(result.totalWithFine()).isEqualByComparingTo(expectedWithFine);
    }

    @Test
    @DisplayName("Empréstimo ainda ativo e atrasado: multa calculada até hoje")
    void shouldApplyFineUntilTodayWhenLoanIsStillActive (){
        int loanDays = 3;
        LocalDate loanDate  = LocalDate.now().minusDays(loanDays + 2);
        Loan loan = buildLoan(loanDays,loanDate, null);

        LoanResponseDTO result = calculate(loan);

        assertThat(result.returnDate()).isNull();
        assertThat(result.totalPrice()).isEqualByComparingTo("6.00");
        assertThat(result.totalWithFine()).isEqualByComparingTo("6.12");
    }

    @Test
    @DisplayName("toResponse preenche os dados de usuário e livro")
    void shouldMapUserAndBookData(){
        Loan loan = buildLoan(3, LOAN_DATE, LOAN_DATE.plusDays(3));

        LoanResponseDTO result  = calculate(loan);

        assertThat(result.id()).isEqualTo(100L);
        assertThat(result.userId()).isEqualTo(1L);
        assertThat(result.userName()).isEqualTo("Herbert");
        assertThat(result.bookId()).isEqualTo(10L);
        assertThat(result.bookTitle()).isEqualTo("Clean Code");
        assertThat(result.dailyRate()).isEqualByComparingTo("2.00");
    }
}
