package hansanhha.querydsl.loan.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record LoanResponse(
        UUID userNumber,
        UUID isbn,
        LocalDateTime borrowDate,
        LocalDateTime returnDateDeadline) {
}
