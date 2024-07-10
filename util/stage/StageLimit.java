package common.util.stage;

import common.io.json.JsonClass;
import common.io.json.JsonField;
import common.util.BattleStatic;
import common.util.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@JsonClass
public class StageLimit extends Data implements BattleStatic {
    @JsonField
    public int maxMoney;
    @JsonField
    public int globalCooldown;
    @JsonField(generic = Integer.class)
    public Set<Integer> bannedCatCombo = new HashSet<>();

    public StageLimit(int maxMoney, int globalCooldown, List<Integer> bannedCombo) {
        this.maxMoney = maxMoney;
        this.globalCooldown = globalCooldown;
        this.bannedCatCombo.addAll(bannedCombo);
    }
}
