package hansanhha.querydsl.loan;

import hansanhha.querydsl.book.entity.Book;
import hansanhha.querydsl.book.repository.BookRepository;
import hansanhha.querydsl.loan.dto.LoanHistoryResponse;
import hansanhha.querydsl.loan.dto.LoanResponse;
import hansanhha.querydsl.loan.dto.ReturnBookResponse;
import hansanhha.querydsl.loan.repository.LoanRepository;
import hansanhha.querydsl.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class LoanService {

    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;

    public Slice<LoanHistoryResponse> getCheckoutHistories(UUID memberNumber) {
        return null;
    }

    public LoanResponse checkout(UUID memberNumber, UUID isbn) {
        Book book = bookRepository.fetchUserByIsbn(isbn).orElseThrow(EntityNotFoundException::new);

        book.validate();




    }

    public ReturnBookResponse returns(Long borrowId) {
        return null;
    }

}
