package hansanhha.querydsl.loan.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReturnBookResponse(
        UUID userNumber,
        UUID isbn,
        LocalDateTime returnDate,
        boolean isOverdue,
        LocalDateTime overduePeriod) {
}
