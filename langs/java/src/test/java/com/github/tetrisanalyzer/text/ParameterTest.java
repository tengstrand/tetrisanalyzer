package com.github.tetrisanalyzer.text;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ParameterTest {

    @Test
    public void test() {
        Parameter parameter = Parameter.fromLong("param", 12345);

        assertEquals(Parameter.fromString("param", "12 345"), parameter);
    }
}
