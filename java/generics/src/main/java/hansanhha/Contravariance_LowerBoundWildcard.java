package hansanhha;

import java.util.ArrayList;
import java.util.List;

public class Contravariance_LowerBoundWildcard {
    public static void main(String[] args) {

        // lower bound 지정
        List<? super Integer> lowerBoundList = new ArrayList<Number>();

        // lower bound: 데이터 추가 가능(consumer)
        lowerBoundList.add(0);
        lowerBoundList.add(0);
        lowerBoundList.add(0);

        // lower bound: 데이터 접근 불가능(consumer), 컴파일 오류
//        Number n = lowerBoundList.get(0);
//        Integer i = lowerBoundList.get(0);

        // Object로 읽기 가능
        Object obj = lowerBoundList.get(0);
    }
}
