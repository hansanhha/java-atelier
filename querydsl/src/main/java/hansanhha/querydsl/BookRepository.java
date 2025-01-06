package hansanhha.querydsl;

import hansanhha.querydsl.model.entity.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {

}
