package hansanhha.querydsl.book;

import hansanhha.querydsl.book.entity.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends CrudRepository<Book, Long> {

    Optional<Book> findByIsbn(UUID isbn);
}
