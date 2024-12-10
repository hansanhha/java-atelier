package spring.data.jpa.simple_jpa_repository;

import org.springframework.data.repository.CrudRepository;

public interface SimpleUserCrudRepository extends CrudRepository<SimpleUser, Long> {
}
