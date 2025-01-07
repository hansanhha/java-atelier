package hansanhha.querydsl.loan.entity;

import hansanhha.querydsl.book.entity.Book;
import hansanhha.querydsl.BaseEntity;
import hansanhha.querydsl.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Loan extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_id")
    private User borrower;

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
