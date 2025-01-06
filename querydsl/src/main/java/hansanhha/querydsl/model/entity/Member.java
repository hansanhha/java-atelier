package hansanhha.querydsl.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    private UUID memberNumber;

    private String name;

    private Integer totalBorrowCount;

    private Integer totalOverdueCount;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "borrower")
    private Set<Book> currentBorrowBooks = new HashSet<>(0);

    public Member create(String name) {
        Member member = new Member();
        member.memberNumber = UUID.randomUUID();
        member.name = name;
        member.totalBorrowCount = 0;
        member.totalOverdueCount = 0;
        return member;
    }

    public void borrowBook(Book book) {
        currentBorrowBooks.add(book);
        totalBorrowCount++;
        book.setBorrower(this);
    }
}
