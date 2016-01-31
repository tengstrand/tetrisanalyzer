package com.github.tetrisanalyzer.game;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ColumnStringConcaterTest {


    @Test
    public void concatWithEmptyString() {
        String string =
                " S\n" +
                "|------|\n" +
                "|------|\n" +
                "|x----x|\n" +
                "========\n";

        String result = ColumnStringConcater.concat("", string);

        assertEquals(string, result);
    }

    @Test
    public void concatTwoStrings() {
        String string1 =
                " S\n" +
                "|------|\n" +
                "|------|\n" +
                "|x----x|\n" +
                "========\n";

        String string2 =
                " T\n" +
                "|------|\n" +
                "|----SS|\n" +
                "|x--SSx|\n" +
                "========\n";

        String result = ColumnStringConcater.concat(string1, string2);

        assertEquals(
                " S        T\n" +
                "|------| |------|\n" +
                "|------| |----SS|\n" +
                "|x----x| |x--SSx|\n" +
                "======== ========", result);
    }


}