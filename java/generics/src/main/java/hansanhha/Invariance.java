package hansanhha;

import java.util.ArrayList;
import java.util.List;

public class Invariance {

    public static void main(String[] args) {
        // 자바의 제네릭은 기본적으로 불공변임
        // 따라서 List<Number>는 List<Integer>, List<Long> 등과 아무 관계가 없음
//        List<Number> list1 = new ArrayList<Integer>();
//        List<Number> list2 = new ArrayList<Long>();
//        List<Number> list3 = new ArrayList<Double>();

        // 타입이 완전히 동일해야만 추가(producer), 접근(consumer) 가능
        List<Integer> integerList = new ArrayList<Integer>();
        integerList.add(1);
        Integer integer = integerList.get(0);
        System.out.println(integer);
    }
}
