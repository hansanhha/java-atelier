package hansanhha;

import java.util.ArrayList;
import java.util.List;

public class Variance_UpperBoundWildcard {
    public static void main(String[] args) {

        // 일반적인 타입 매개변수 사용
        List<Integer> normalGenericsList = new ArrayList<>();
        normalGenericsList.add(0);
        normalGenericsList.add(0);

        // upper bound: 데이터 접근 가능(consumer)
        List<? extends Number> upperBoundList = normalGenericsList;
        Number n = upperBoundList.get(0);
        System.out.println(n);

        // upper bound: 데이터 추가 불가능(producer), 컴파일 오류
//        upperBoundList.add(1L);
//        upperBoundList.add(1.1);
//        upperBoundList.add(1);
    }
}
