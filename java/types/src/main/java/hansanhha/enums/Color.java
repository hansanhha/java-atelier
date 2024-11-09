package hansanhha.enums;

import java.util.HashMap;
import java.util.Map;

public enum Color {

    RED, GREEN, BLUE;

    static final Map<String, Color> colorMap = new HashMap<>();

    static {
        for (Color c : Color.values()) {
            colorMap.put(c.toString(), c);
        }
    }

}
