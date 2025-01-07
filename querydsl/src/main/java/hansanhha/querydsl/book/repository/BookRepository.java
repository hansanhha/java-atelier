package hansanhha.querydsl.book.repository;

import hansanhha.querydsl.book.entity.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends CrudRepository<Book, Long>, BookCategoryRepository{

    Optional<Book> findByIsbn(UUID isbn);
}
