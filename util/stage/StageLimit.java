package common.util.stage;

import common.io.json.JsonClass;
import common.io.json.JsonField;
import common.util.BattleStatic;
import common.util.Data;

import java.util.HashSet;
import java.util.List;

@JsonClass(noTag = JsonClass.NoTag.LOAD)
public class StageLimit extends Data implements BattleStatic {
    public int maxMoney = 0;
    public int globalCooldown = 0;
    public int[] cdMultiplier = { 100, 100, 100, 100, 100, 100 };
    public int[] costMultiplier = { 100, 100, 100, 100, 100, 100 };
    public boolean coolStart = false;
    @JsonField(generic = Integer.class)
    public HashSet<Integer> bannedCatCombo = new HashSet<>();

    public StageLimit() {
    }

    public StageLimit(int maxMoney, int globalCooldown, List<Integer> bannedCombo) {
        this.maxMoney = maxMoney;
        this.globalCooldown = globalCooldown;
        this.bannedCatCombo.addAll(bannedCombo);
    }

    public StageLimit clone() {
        StageLimit sl = new StageLimit();
        sl.maxMoney = maxMoney;
        sl.globalCooldown = globalCooldown;
        sl.bannedCatCombo.addAll(bannedCatCombo);
        return sl;
    }

    public StageLimit combine(StageLimit second) {
        StageLimit combined = new StageLimit();
        combined.maxMoney = second.maxMoney == 0 ? maxMoney : second.maxMoney;
        combined.globalCooldown = second.globalCooldown == 0 ? globalCooldown : second.globalCooldown;
        combined.bannedCatCombo.addAll(bannedCatCombo);
        combined.bannedCatCombo.addAll(second.bannedCatCombo);
        return combined;
    }
}
