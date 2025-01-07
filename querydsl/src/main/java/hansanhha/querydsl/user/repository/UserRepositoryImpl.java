package hansanhha.querydsl.user.repository;

import hansanhha.querydsl.book.entity.QBook;
import hansanhha.querydsl.loan.entity.QWaitList;
import hansanhha.querydsl.user.entity.QUser;
import hansanhha.querydsl.user.entity.User;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryImpl extends QuerydslRepositorySupport implements UserRepositoryCustom {

    public UserRepositoryImpl() {
        super(User.class);
    }

    @Override
    public Optional<User> fetchByUserNumber(UUID userNumber) {
        QUser user = QUser.user;
        QBook book = QBook.book;
        QWaitList waitList = QWaitList.waitList;

        User foundUser = from(user)
                .leftJoin(user.currentBorrowBooks, book).fetchJoin()
                .leftJoin(user.currentWaitList, waitList).fetchJoin()
                .where(user.userNumber.eq(userNumber))
                .fetchFirst();

        return Optional.ofNullable(foundUser);
    }
}
