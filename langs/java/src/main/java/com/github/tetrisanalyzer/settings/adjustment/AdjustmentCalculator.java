package com.github.tetrisanalyzer.settings.adjustment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdjustmentCalculator {

    public static Adjustments calculate(String piece, AdjustmentDxDy... dxdyList) {
        return calculate(piece, Arrays.asList(dxdyList));
    }

    /**
     * Calculates the delta of the movement (dx, dy) from one rotation
     * to the next (the last rotation will refer to the first).
     */
    public static Adjustments calculate(String piece, List<AdjustmentDxDy> dxdyList) {
        if (dxdyList.isEmpty()) {
            return new Adjustments(piece, dxdyList);
        }
        int modulus = new int[] { -1, 0, 1, -1, 3,  }[dxdyList.size()];
        List<Integer> dx = new ArrayList<>();
        List<Integer> dy = new ArrayList<>();

        for (int i=0; i<dxdyList.size(); i++) {
            AdjustmentDxDy current = dxdyList.get(i);
            AdjustmentDxDy next = dxdyList.get((i + 1) & modulus);
            dx.add(next.dx - current.dx);
            dy.add(next.dy - current.dy);
        }
        return new Adjustments(piece, dxdyList.get(0).dx, dxdyList.get(0).dy, dx, dy, dxdyList);
    }
}
