package hansanhha;

public class StringUtils {

    // 불필요한 코드 (sonarqube 경고할 가능성이 있음)
    public static String toUpperCase(String str) {
        if (str == null) {
            return null;
        }
        return str.toUpperCase();
    }

    // 중복된 코드
    public static String toLowerCase(String str) {
        if (str == null) {
            return null;
        }
        return str.toLowerCase();
    }
}
