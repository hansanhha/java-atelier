package hansanhha.querydsl.model.entity;

import hansanhha.querydsl.model.vo.BookMainCategory;
import hansanhha.querydsl.model.vo.BookMiddleCategory;
import hansanhha.querydsl.model.vo.BookSubCategory;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class BookCategory {

    @Enumerated(EnumType.STRING)
    private BookMainCategory mainCategory;

    @Enumerated(EnumType.STRING)
    private BookMiddleCategory middleCategory;

    @Enumerated(EnumType.STRING)
    private BookSubCategory subCategory;
}
