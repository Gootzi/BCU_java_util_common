package common.battle.attack;

import common.CommonStatic;
import common.battle.entity.AbEntity;
import common.battle.entity.Entity;
import common.system.P;
import common.system.fake.FakeGraphics;
import common.util.pack.NyCastle.NyType;

import java.util.HashSet;
import java.util.Set;

public class ContWaveCanon extends ContWaveAb {

	private final int canid;

	// used only by normal and zombie cannon
	public ContWaveCanon(AttackWave a, float p, int id) {
		super(a, p, CommonStatic.getBCAssets().atks[id].getEAnim(NyType.ATK), 9, -3);
		canid = id;
		soundEffect = SE_CANNON[canid][1];

		waves = new HashSet<>();
		waves.add(this);
		// hitframe offset + (waves attack period) * (number of waves - 1) + ending linger
		maxt = 3 + (W_TIME + 1) * (a.proc.WAVE.lv + 1 - 1) + 4;

		if (id != 0) {
			anim.setTime(1);
			maxt -= 1;
		}
	}

	public ContWaveCanon(AttackWave a, float p, int id, int maxTime, Set<ContWaveAb> waves) {
		super(a, p, CommonStatic.getBCAssets().atks[id].getEAnim(NyType.ATK), 9, 0);
		canid = id;
		soundEffect = SE_CANNON[canid][1];

		this.waves = waves;
		this.waves.add(this);
		maxt = maxTime;

		if (id != 0) {
			anim.setTime(1);
			maxt -= 1;
		}
	}

	@Override
	public void draw(FakeGraphics gra, P p, float psiz) {
		if (t < 0)
			return;
		drawAxis(gra, p, psiz);
		if (canid == 0)
			psiz *= 1.25;
		else
			psiz *= 0.5 * 1.25;
		P pus = canid == 0 ? new P(9, 40) : new P(-72, 0);
		anim.draw(gra, p.plus(pus, -psiz), psiz * 2);
	}

	public float getSize() {
		return 2.5f;
	}

	@Override
	public void update() {
		tempAtk = false;
		// guessed attack point compared from BC
		int attack = 2;
		// guessed wave block time compared from BC
		if (t == 0)
			CommonStatic.setSE(soundEffect);
		if (t >= 1 && t <= attack) {
			atk.capture();
			for (AbEntity e : atk.capt)
				if ((e.getAbi() & AB_WAVES) > 0) {
					if (e instanceof Entity)
						((Entity) e).anim.getEff(STPWAVE);
					deactivate();
					return;
				}
		}
		if (!activate)
			return;
		if (t == W_TIME && atk.getProc().WAVE.lv > 0)
			nextWave();
		if (t >= attack) {
			sb.getAttack(atk);
			tempAtk = true;
		}
		if (maxt == t)
			deactivate();
		if (t >= 0 && !anim.done())
			anim.update(false);
		t++;
	}

	@Override
	public void updateAnimation() {
		if (t >= 0 && !anim.done())
			anim.update(false);
	}

	@Override
	protected void nextWave() {
		float np = pos - NYRAN[canid];
		new ContWaveCanon(new AttackWave(atk.attacker, atk, np, NYRAN[canid]), np, canid, maxt - t, waves);
	}

	@Override
	public boolean IMUTime() {
		return false;
	}
}
