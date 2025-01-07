package hansanhha.querydsl.loan;

import hansanhha.querydsl.loan.dto.LoanHistoryResponse;
import hansanhha.querydsl.loan.dto.LoanResponse;
import hansanhha.querydsl.loan.dto.ReturnBookResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository repository;

    public Slice<LoanHistoryResponse> getCheckoutHistories(String memberNumber) {
        return null;
    }

    public LoanResponse checkout(String memberNumber, String isbn) {
        return null;
    }

    public ReturnBookResponse returns(Long borrowId) {
        return null;
    }

}
