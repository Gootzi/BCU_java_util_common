package common.util.stage;

import common.CommonStatic;
import common.battle.StageBasis;
import common.battle.entity.EEnemy;
import common.pack.Identifier;
import common.pack.UserProfile;
import common.util.BattleObj;
import common.util.stage.SCDef.Line;
import common.util.unit.AbEnemy;

public class EStage extends BattleObj {

	public final Stage s;
	public final Limit lim;
	public final int[] num, rem, first;
	public final float mul;
	public final int star;
	public final int[] killCounter;

	private StageBasis b;

	public EStage(Stage st, int stars) {
		s = st;
		star = stars;
		st.validate();
		Line[] datas = s.data.getSimple();
		rem = new int[datas.length];
		num = new int[datas.length];
		first = new int[datas.length];
		for (int i = 0; i < rem.length; i++)
			num[i] = datas[i].number;
		lim = st.getLim(star);
		mul = st.getCont().stars[star] * 0.01f;

		killCounter = new int[s.data.datas.length];

		for(int i = 0; i < killCounter.length; i++) {
			if(s.data.datas[i].castle_0 != 0) {
				killCounter[i] = s.data.datas[i].kill_count;
			}
		}
	}

	/**
	 * add n new enemies to StageBasis
	 */
	public EEnemy allow() {
		if(s.trail && s.timeLimit != 0 && s.timeLimit * 60 * 30 - b.time < 0)
			return null;

		for (int i = 0; i < rem.length; i++) {
			Line data = s.data.getSimple(i);

			if (inHealth(data, i) && s.data.allow(b, data.group, Identifier.getOr(data.enemy, AbEnemy.class)) && rem[i] <= 1 && rem[i] >= 0 && num[i] != -1 && killCounter[i] == 0) {
				if(data.respawn_0 >= data.respawn_1)
					rem[i] = data.respawn_0;
				else
					rem[i] = data.respawn_0 + (int) (b.r.nextFloat() * (data.respawn_1 - data.respawn_0));

				rem[i]++;

				if (num[i] > 0) {
					num[i]--;

					if (num[i] == 0)
						num[i] = -1;
				}

				if (data.boss >= 1 && !b.shock)
					b.shock = true;

				if (CommonStatic.getConfig().shake && data.boss == 2 && b.shakeCoolDown[1] == 0) {
					b.shake = SHAKE_MODE_BOSS;
					b.shakeDuration = SHAKE_MODE_BOSS[SHAKE_DURATION];
					b.shakeCoolDown[1] = SHAKE_MODE_BOSS[SHAKE_COOL_DOWN];
				}

				float multi = (data.multiple == 0 ? 100 : data.multiple) * mul * 0.01f;
				float mulatk = (data.multiple == 0 ? 100 : data.mult_atk) * mul * 0.01f;

				AbEnemy e = Identifier.getOr(data.enemy, AbEnemy.class);

				EEnemy ee = e.getEntity(b, data, multi, mulatk, data.layer_0, data.layer_1, data.boss);

				ee.group = data.group;
				return ee;
			}
		}
		return null;
	}

	public void assign(StageBasis sb) {
		b = sb;
		Line[] datas = s.data.getSimple();
		for (int i = 0; i < rem.length; i++) {
			rem[i] = datas[i].spawn_0;

			if (Math.abs(datas[i].spawn_0) < Math.abs(datas[i].spawn_1))
				rem[i] += (int) ((datas[i].spawn_1 - datas[i].spawn_0) * b.r.nextFloat());

			if (s.isBCstage && datas[i].castle_0 < 100 && rem[i] > 0 && !s.trail)
				rem[i] = 0;
		}
	}

	/**
	 * get the Entity representing enemy base, return null if none
	 */
	public EEnemy base(StageBasis sb) {
		int ind = num.length - 1;
		if (ind < 0)
			return null;
		Line data = s.data.getSimple(ind);
		if (data.castle_0 == 0) {
			num[ind] = -1;
			float multi = data.multiple * mul * 0.01f;

			if(sb.st.trail)
				multi = Integer.MAX_VALUE;

			float mulatk = data.mult_atk * mul * 0.01f;

			Identifier<AbEnemy> enemy;

			if(sb.st.isAkuStage() && CommonStatic.getConfig().levelLimit == 0) {
				enemy = UserProfile.getBCData().enemies.get(575).id;
			} else {
				enemy = data.enemy;
			}

			AbEnemy e = Identifier.getOr(enemy, AbEnemy.class);
			return e.getEntity(sb, this, multi, mulatk, data.layer_0, data.layer_1, data.boss >= 1 ? -2 : -1);
		}
		return null;
	}

	/**
	 * return true if there is still boss in the base
	 */
	public boolean hasBoss() {
		for (int i = 0; i < rem.length; i++) {
			Line data = s.data.getSimple(i);

			if (data.boss == 1 && num[i] > 0)
				return true;
		}

		return false;
	}

	public void update() {
		for (int i = 0; i < rem.length; i++) {
			Line data = s.data.getSimple(i);

			if (inHealth(data, i) && rem[i] < 0)
				rem[i] *= -1;

			if (rem[i] > 0)
				rem[i]--;
		}
	}

	private boolean inHealth(Line line, int index) {
		int c0 = !s.trail ? Math.min(line.castle_0, 100) : line.castle_0;
		int c1 = line.castle_1;

		float d = !s.trail ? b.getEBHP() : (b.ebase.maxH - b.ebase.health);

		boolean inRange = c0 >= c1 ? (s.trail ? d >= c0 : d <= c0) : (d > c0 && d <= c1);

		if (b.ebase instanceof EEnemy && inRange && first[index] == 0 && !s.trail) {
			first[index] = -1;

			return false;
		}

		return inRange;
	}

}
