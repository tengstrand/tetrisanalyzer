package com.github.tetrisanalyzer.settings;

import com.github.tetrisanalyzer.settings.adjustment.AdjustmentCalculator;
import com.github.tetrisanalyzer.settings.adjustment.AdjustmentDxDy;
import com.github.tetrisanalyzer.settings.adjustment.Adjustments;
import org.junit.Test;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;

public class AdjustmentCalculatorTest {

    /**
     * ----
     * -xx-
     * -xx-
     * ----
     * (1,1)
     */
    @Test
    public void adjustO() {
        AdjustmentDxDy adjustment = new AdjustmentDxDy(1, 1);
        Adjustments result = AdjustmentCalculator.calculate("O", adjustment);

        assertEquals(new Adjustments("O", 1, Arrays.asList(0), Arrays.asList(0), Arrays.asList(adjustment)), result);
    }

    /**
     * ----  ----
     * -xx-  ----
     * --xx  ---x
     * ----  --xx
     * ----  --x-
     * (1,1) (2,2)
     */
    @Test
    public void adjustZ() {
        AdjustmentDxDy adjustment1 = new AdjustmentDxDy(1, 1);
        AdjustmentDxDy adjustment2 = new AdjustmentDxDy(2, 2);
        Adjustments result = AdjustmentCalculator.calculate("Z", adjustment1, adjustment2);

        assertEquals(new Adjustments("Z", 1, Arrays.asList(1, -1), Arrays.asList(1, -1), Arrays.asList(adjustment1, adjustment2)), result);
    }

    /**
     * ----  --x-  --x-  --x-
     * -xxx  --xx  -xxx  -xx-
     * --x-  --x-  ----  --x-
     * ----  ----  ----  ----
     * (1,1) (2,0) (1,0) (1,0)
     */
    @Test
    public void adjustT() {
        AdjustmentDxDy adjustment1 = new AdjustmentDxDy(1, 1);
        AdjustmentDxDy adjustment2 = new AdjustmentDxDy(2, 0);
        AdjustmentDxDy adjustment3 = new AdjustmentDxDy(1, 0);
        AdjustmentDxDy adjustment4 = new AdjustmentDxDy(1, 0);
        Adjustments result = AdjustmentCalculator.calculate("T", adjustment1, adjustment2, adjustment3, adjustment4);

        assertEquals(new Adjustments("T", 1, Arrays.asList(1, -1, 0, 0), Arrays.asList(-1, 0, 0, 1),
                Arrays.asList(adjustment1, adjustment2, adjustment3, adjustment4)), result);
    }

    @Test
    public void export() {
        AdjustmentDxDy adjustment1 = new AdjustmentDxDy(1, 1);
        AdjustmentDxDy adjustment2 = new AdjustmentDxDy(2, 0);
        String result = AdjustmentCalculator.calculate("Z", adjustment1, adjustment2).export();

        assertEquals("[[1,1] [2,0]]", result);
    }
}
