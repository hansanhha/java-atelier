package com.hansanhha.types.annoations;

import com.hansanhha.types.annotations.A;
import com.hansanhha.types.annotations.B;
import com.hansanhha.types.annotations.C;
import org.junit.jupiter.api.Test;

public class AnnotationTest {

    @Test
    void declarationAnnotation_And_TypeAnnotation() {
        @A int @B [] @C [] f;
    }
}
