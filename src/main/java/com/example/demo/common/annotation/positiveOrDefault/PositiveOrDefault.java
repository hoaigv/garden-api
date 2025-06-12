package com.example.demo.common.annotation.positiveOrDefault;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PositiveOrDefault {
    int defaultValue() default 0;
}
