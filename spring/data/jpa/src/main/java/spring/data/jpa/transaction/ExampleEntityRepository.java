package spring.data.jpa.transaction;

import org.springframework.data.repository.CrudRepository;

public interface ExampleEntityRepository extends CrudRepository<ExampleEntity, Long> {
}
