package common.battle;

import common.util.Data;

import java.util.Map;

public class CannonLevelCurve extends Data {
    public enum PART {
        CANNON,
        BASE,
        DECORATION
    }

    private static final byte MIN_VALUE = 0;
    private static final byte MAX_VALUE = 1;

    public final int max;
    private final PART part;

    private final Map<Integer, int[][]> curveMap;

    public CannonLevelCurve(Map<Integer, int[][]> curveMap, int max, PART part) {
        this.curveMap = curveMap;
        this.max = max;
        this.part = part;
    }

    public float applyFormula(int type, int level) {
        float v = applyFormulaRaw(type, level);
        switch (part) {
            case CANNON:
                switch (type) {
                    case BASE_RANGE:
                        return (int) v / 4f;
                    case BASE_HEALTH_PERCENTAGE:
                        return v / 10f;
                    case BASE_HOLY_ATK_SURFACE:
                    case BASE_HOLY_ATK_UNDERGROUND:
                        return v / 1000f;
                    default:
                        return v;
                }
            case BASE:
            case DECORATION:
                return 1f - v / 10000f;
            default:
                return v;
        }
    }

    public float applyFormulaRaw(int type, int level) {
        if (level <= 0 || !curveMap.containsKey(type)) {
            System.out.println("Warning : Invalid level " + level);
            return 0;
        }

        int[][] curve = curveMap.get(type);
        int index = Math.min((level - 1) / 10, curve[0].length - 1);

        int min = curve[MIN_VALUE][index];
        int max = curve[MAX_VALUE][index];

        float v;

        if (level <= 10 && part == PART.CANNON) {
            v = min + (max - min) * (level - 1) / 9f;
        } else {
            v = min + (max - min) * (level % 10) / 10f;
        }

        return v;
    }
}
