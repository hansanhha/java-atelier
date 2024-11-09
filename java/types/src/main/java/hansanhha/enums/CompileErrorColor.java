package hansanhha.enums;

import java.util.HashMap;
import java.util.Map;

public enum CompileErrorColor {

    RED, GREEN, BLUE;

    // compile error; "Accessing static field from enum constructor is not allowed"
//    Color() {
//        colorMap.put(toString(), this);
//    }

    static final Map<String, CompileErrorColor> colorMap = new HashMap<>();
}
