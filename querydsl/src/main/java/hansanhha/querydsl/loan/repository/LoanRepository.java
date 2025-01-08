package hansanhha.querydsl.loan.repository;

import hansanhha.querydsl.loan.entity.Loan;
import org.springframework.data.repository.CrudRepository;

public interface LoanRepository extends CrudRepository<Loan, Long> {
}
