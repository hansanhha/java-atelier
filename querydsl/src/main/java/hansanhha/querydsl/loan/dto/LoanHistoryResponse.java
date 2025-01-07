package hansanhha.querydsl.loan.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record LoanHistoryResponse(
        UUID userNumber,
        UUID isbn,
        boolean isUseExtensionReturnDate,
        boolean isOverdue,
        LocalDateTime borrowDate,
        LocalDateTime returnDateDeadline,
        LocalDateTime returnDate,
        LocalDateTime overduePeriod) {
}
