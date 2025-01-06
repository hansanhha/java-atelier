package hansanhha.querydsl.model.entity;

import hansanhha.querydsl.model.vo.BookStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Book {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String isbn;

    private String author;

    @Enumerated(EnumType.STRING)
    private BookStatus status;

    @Embedded
    private BookCategory category;

    @Setter(AccessLevel.PROTECTED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_id")
    private Member borrower;
}
