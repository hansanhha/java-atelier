package hansanhha.querydsl.loan;

import hansanhha.querydsl.loan.entity.Loan;
import org.springframework.data.repository.CrudRepository;

public interface LoanRepository extends CrudRepository<Loan, Long> {
}
