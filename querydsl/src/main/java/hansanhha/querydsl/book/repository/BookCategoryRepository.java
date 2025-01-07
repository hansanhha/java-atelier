package hansanhha.querydsl.book.repository;

import hansanhha.querydsl.book.entity.Book;
import hansanhha.querydsl.book.entity.BookCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookCategoryRepository {

    Page<Book> findAllByCategory(BookCategory category, Pageable pageable);
}
