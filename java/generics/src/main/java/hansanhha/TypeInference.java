package hansanhha;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class TypeInference {

    public static void main(String[] args) {

        // 1. 다이아몬드 연산자 타입 추론
        // 좌변의 List<String>을 기반으로 타입 추론에 의해 타입 매개변수 <String> 결정
        List<String> diamondOperator = new ArrayList<>();

        // 2. 제네릭 메서드 타입 추론
        // 전달된 인자 값의 타입을 기반으로 제네릭 메서드 타입 매개변수 타입 추론
        String str = genericMethodTypeInference("hello"); // 타입 추론: T는 String으로 결정
        Integer integer = genericMethodTypeInference(1); // 타입 추론: T는 Integer로 결정
        boolean bool = genericMethodTypeInference(false); // 타입 추론: T는 Boolean으로 결정

        // 3. 타겟 타입
        // 타겟 타입은 표현식의 결과가 사용되는 문맥을 기반으로 타입을 추론함
        /*
            3.1 변수 할당
            - 변수의 타입이 명시된 경우 타겟 타입이 변수의 선언된 타입으로 설정됨
            - 변수 Comparator<String>을 통해 람다 표현식 (s1, s2)의 매개변수 타입을 String으로 추론
         */
        Comparator<String> stringComparator = (s1, s2) -> s1.length() - s2.length();
        /*
            3.2 메서드 호출 인자 (제네릭 메서드 아님)
            - 메서드의 파라미터 타입이 제네릭이거나, 함수형 인터페이스를 사용하는 경우
              인자의 타겟 타입이 메서드의 파라미터 타입으로 결정됨
            - sort 메서드는 Comparator<String>을 요구하므로(List<String>으로 인해 list의 타입 매개변수는 <String>임)
              (s1, s2) 타입은 String으로 추론됨
         */
        List<String> list = Arrays.asList("one", "two", "three");
        list.sort((s1, s2) -> s1.compareToIgnoreCase(s2));

        /*
            3.3 반환 타입
            - 타겟 타입이 메서드의 반환 타입에 의해 결정되는 경우
            - Callable<Integer>의 call 메서드는 Integer를 반환하므로, 람다 표현식의 결과 타입이 Integer로 추론됨
         */
        Callable<Integer> callable =() -> 42;

        // 3.4 삼항 연산자
        Integer value = true ? 1 : 2;

        /*
            4. 람다 표현식과 타겟 타입
            - 함수형 인터페이스의 함수 디스크립터(메서드 시그니처) 확인
            - 람다 표현식의 매개변수 타입과 리턴 타입을 타겟 타입에 맞게 추론
         */
        Runnable run = () -> System.out.println("Runnable Interface");
        Predicate<Integer> p = (i) -> i < 100;
    }

    public static <T> T genericMethodTypeInference(T a) {
        return a;
    }
}
