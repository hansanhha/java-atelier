package hansanhha.querydsl.book.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hansanhha.querydsl.book.entity.Book;
import hansanhha.querydsl.book.entity.BookCategory;
import hansanhha.querydsl.book.entity.QBook;
import hansanhha.querydsl.book.vo.BookMainCategory;
import hansanhha.querydsl.book.vo.BookMiddleCategory;
import hansanhha.querydsl.book.vo.BookSubCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static hansanhha.querydsl.book.repository.BookRepositoryImpl.BookCategoryExpressions.*;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookCategoryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Book> findAllByCategory(BookCategory category, Pageable pageable) {
        QBook book = QBook.book;

        BooleanExpression categoryCondition = buildCategoryCondition(book, category);

        List<Book> books = queryFactory
                .selectFrom(book)
                .where(categoryCondition)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();


        return new PageImpl<>(books, pageable, books.size());
    }

    private BooleanExpression buildCategoryCondition(QBook book, BookCategory category) {

        BooleanExpression condition = null;

        BookMainCategory mainCategory = category.getMainCategory();
        BookMiddleCategory middleCategory = category.getMiddleCategory();
        BookSubCategory subCategory = category.getSubCategory();

        if (mainCategory != null) {
            condition = mainCategoryEq(book, mainCategory);
        }

        if (middleCategory != null) {
            if (mainCategory != null && !mainCategory.contains(middleCategory)) {
                throw new IllegalArgumentException("주요 카테고리와 중간 카테고리가 잘못 지정되었습니다");
            }

            condition = (condition == null)
                    ? middleCategoryEq(book, middleCategory)
                    : condition.and(middleCategoryEq(book, middleCategory));
        }

        if (subCategory != null) {
            if (middleCategory != null && !middleCategory.contains(subCategory)) {
                throw new IllegalArgumentException("중간 카테고리와 서브 카테고리가 잘못 지정되었습니다");
            }

            condition = (condition == null)
                    ? subCategoryEq(book,subCategory)
                    : condition.and(subCategoryEq(book,subCategory));
        }

        return condition;
    }

    record BookCategoryExpressions() {

        static BooleanExpression mainCategoryEq(QBook book, BookMainCategory mainCategory) {
            return book.category.mainCategory.eq(mainCategory);
        }

        static BooleanExpression middleCategoryEq(QBook book, BookMiddleCategory middleCategory) {
            return book.category.middleCategory.eq(middleCategory);
        }

        static BooleanExpression subCategoryEq(QBook book, BookSubCategory subCategory) {
            return book.category.subCategory.eq(subCategory);
        }
    }


}
