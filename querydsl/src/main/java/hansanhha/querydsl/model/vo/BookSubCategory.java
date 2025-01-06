package hansanhha.querydsl.model.vo;

import lombok.RequiredArgsConstructor;

import static hansanhha.querydsl.model.vo.BookMiddleCategory.*;

@RequiredArgsConstructor
public enum BookSubCategory {

    KOREAN_ENCYCLOPEDIA(ENCYCLOPEDIA, "031", "한국어"),
    JAPANESE_ENCYCLOPEDIA(ENCYCLOPEDIA, "033", "일본어"),

    DEDUCTIVE_METHOD(LOGIC, "171", "연역법"),
    INDUCTION(LOGIC, "172", "귀납법"),

    BIBLE(CHRISTIANITY, "233", "성서(성경)"),

    ARMY(MILITARY_SCIENCE, "396", "육군"),
    NAVY(MILITARY_SCIENCE, "397", "해군"),
    AIR_FORCE(MILITARY_SCIENCE, "398", "공군");

    private final BookMiddleCategory parentCategory;
    private final String code;
    private final String displayName;
}
