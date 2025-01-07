package hansanhha.querydsl.book;

import hansanhha.querydsl.book.dto.BookResponse;
import hansanhha.querydsl.book.entity.Book;
import hansanhha.querydsl.book.entity.BookCategory;
import hansanhha.querydsl.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BookUserService {

    private final BookRepository bookRepository;

    public Page<BookResponse> getBooksByCategory(BookCategory category, Pageable pageable) {
        Page<Book> books = bookRepository.findAllByCategory(category, pageable);

        return books.map(BookResponse::from);
    }

}
