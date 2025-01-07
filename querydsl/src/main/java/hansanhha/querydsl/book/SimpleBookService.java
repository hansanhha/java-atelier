package hansanhha.querydsl.book;

import hansanhha.querydsl.book.dto.BookResponse;
import hansanhha.querydsl.book.entity.Book;
import hansanhha.querydsl.book.entity.BookCategory;
import hansanhha.querydsl.book.vo.BookStatus;
import hansanhha.querydsl.book.vo.BookSubCategory;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class SimpleBookService {

    private final BookRepository repository;

    public BookResponse create(String title, String author, BookSubCategory category) {
        Book book = Book.builder()
                .isbn(UUID.randomUUID())
                .title(title)
                .author(author)
                .category(BookCategory.from(category))
                .status(BookStatus.BORROW_AVAILABLE)
                .build();

        Book saved = repository.save(book);
        return BookResponse.from(saved);
    }

    public BookResponse inactive(UUID isbn) {
        return repository.findByIsbn(isbn)
                .map(BookResponse::from)
                .orElseThrow(EntityNotFoundException::new);
    }
}
