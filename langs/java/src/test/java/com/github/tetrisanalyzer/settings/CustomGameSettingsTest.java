package com.github.tetrisanalyzer.settings;

import com.esotericsoftware.yamlbeans.YamlException;
import com.github.tetrisanalyzer.settings.adjustment.AdjustmentCalculator;
import com.github.tetrisanalyzer.settings.adjustment.AdjustmentDxDy;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class CustomGameSettingsTest {

    @Test
    public void create() throws YamlException {
        String settings = "id: Standard\n" +
                "description: Standard Tetris 2007 June 4 (Colin Fahey)\n" +
                "url: http://colinfahey.com/tetris/tetris.html\n" +
                "class: com.github.tetrisanalyzer.settings.StandardGameSettings\n" +
                "sliding: on\n" +
                "rotation: anticlockwise\n" +
                "piece start position on standard board: [3,0]\n" +
                "O: [[1,1]]\n" +
                "I: [[0,1] [2,0]]\n" +
                "S: [[1,1] [2,0]]\n" +
                "Z: [[1,1] [2,0]]\n" +
                "L: [[1,1] [2,0] [1,0] [1,0]]\n" +
                "J: [[1,1] [2,0] [1,0] [1,0]]\n" +
                "T: [[1,1] [2,0] [1,0] [1,0]]";

        CustomGameSettings gameSettings = CustomGameSettings.fromString(settings);

        assertEquals("Standard", gameSettings.id);
        assertEquals("anticlockwise", gameSettings.rotationDirection.toString());
        assertEquals("Standard Tetris 2007 June 4 (Colin Fahey)", gameSettings.description);
        assertEquals("com.github.tetrisanalyzer.settings.StandardGameSettings", gameSettings.clazz.getCanonicalName());
        assertEquals(3, gameSettings.pieceStartX);
        assertEquals(0, gameSettings.pieceStartY);
        assertTrue(gameSettings.slidingEnabled);
        assertEquals(gameSettings.pieceAdjustments[1], AdjustmentCalculator.calculate("O", dxdy(1, 1)));
        assertEquals(gameSettings.pieceAdjustments[2], AdjustmentCalculator.calculate("I", dxdy(0, 1), dxdy(2, 0)));
        assertEquals(gameSettings.pieceAdjustments[3], AdjustmentCalculator.calculate("S", dxdy(1, 1), dxdy(2, 0)));
        assertEquals(gameSettings.pieceAdjustments[4], AdjustmentCalculator.calculate("Z", dxdy(1, 1), dxdy(2, 0)));
        assertEquals(gameSettings.pieceAdjustments[5], AdjustmentCalculator.calculate("L", dxdy(1, 1), dxdy(2, 0), dxdy(1, 0), dxdy(1, 0)));
        assertEquals(gameSettings.pieceAdjustments[6], AdjustmentCalculator.calculate("J", dxdy(1, 1), dxdy(2, 0), dxdy(1, 0), dxdy(1, 0)));
        assertEquals(gameSettings.pieceAdjustments[7], AdjustmentCalculator.calculate("T", dxdy(1,1), dxdy(2,0), dxdy(1,0), dxdy(1,0)));
    }

    private AdjustmentDxDy dxdy(int dx, int dy) {
        return new AdjustmentDxDy(dx, dy);
    }
}
