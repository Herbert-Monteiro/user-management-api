package com.herbertmonteiro.usermanagementapi.repository;

import com.herbertmonteiro.usermanagementapi.entity.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoanRepository extends  JpaRepository<Loan, Long> {
    List<Loan> findByReturnDateIsNull();
    boolean existsByBookIdAndReturnDateIsNull(Long bookId);
    List<Loan> findByUserId(Long userId);
    Page<Loan> findAll(Pageable pageable);
    Page<Loan> findByReturnDateIsNull(Pageable pageable);
}
