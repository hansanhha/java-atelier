package hansanhha.querydsl.user.repository;

import hansanhha.querydsl.user.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long>, UserRepositoryCustom {

}
