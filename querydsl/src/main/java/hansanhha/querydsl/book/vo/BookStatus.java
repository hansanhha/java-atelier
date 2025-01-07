package hansanhha.querydsl.book.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BookStatus {

    BORROW_AVAILABLE("대여 가능"),
    BORROW_NOT_AVAILABLE("대여 불가능"),
    BORROWED("대여 중"),
    OVERDUE("연체됨");

    private final String displayName;
}
