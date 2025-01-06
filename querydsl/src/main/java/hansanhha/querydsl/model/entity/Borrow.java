package hansanhha.querydsl.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Borrow {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_id")
    private Member borrower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    private Integer extensionBookReturnCount;

    private LocalDateTime bookBorrowDate;
    private LocalDateTime bookReturnDeadlineDate;

    private LocalDateTime bookReturnDate;
    private Boolean isOverdue;
    private LocalDateTime overduePeriod;
}
