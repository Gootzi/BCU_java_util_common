package common.battle.entity;

import common.battle.attack.AttackAb;
import common.util.BattleObj;
import common.util.unit.Trait;

import java.util.ArrayList;

public abstract class AbEntity extends BattleObj {

	/**
	 * health = Unit's current health.
	 * maxH = Unit's maximum HP. Used to limit healing and any effects that require % of Entity's HP.
	 */
	public long health, maxH;
	/**
	 * Direction/Faction of entity. -1 is Cat unit, 1 is Enemy Unit
	 */
	public int dire;
	/**
	 * Current Position of this Entity
	 */
	public float pos;
	/**
	 * Last position where entity moved without interruption
	 */
	public float lastPosition;

	protected AbEntity(int h) {
		if (h <= 0)
			h = 1;
		health = maxH = h;
	}

	public void added(int d, float p) {
		pos = p;
		lastPosition = p;
		dire = d;
	}

	public abstract void damaged(AttackAb atk);

	public abstract int getAbi();

	public abstract boolean isBase();

	public abstract void postUpdate();

	public abstract boolean ctargetable(ArrayList<Trait> t, Entity attacker, boolean targetOnly);

	public abstract int touchable();

	public abstract void update();

	public abstract void update2();

	public abstract void updateAnimation();
}
