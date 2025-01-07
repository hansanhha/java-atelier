package hansanhha.querydsl.book.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static hansanhha.querydsl.book.vo.BookMainCategory.*;

@Getter
@RequiredArgsConstructor
public enum BookMiddleCategory {

    ENCYCLOPEDIA(ETC, "030", "백과사전"),
    LOGIC(PHILOSOPHY, "170", "논리학"),
    CHRISTIANITY(RELIGION, "230", "기독교"),
    MILITARY_SCIENCE(SOCIAL_SCIENCE, "390", "군사학");

    private final BookMainCategory parentCategory;
    private final String code;
    private final String displayName;
}
