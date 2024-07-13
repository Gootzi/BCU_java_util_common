package common.battle.entity;

import common.battle.StageBasis;
import common.battle.Treasure;
import common.battle.attack.*;
import common.battle.data.MaskAtk;
import common.battle.data.MaskUnit;
import common.battle.data.Orb;
import common.battle.data.PCoin;
import common.pack.UserProfile;
import common.util.BattleObj;
import common.util.Data;
import common.util.anim.EAnimU;
import common.util.unit.Level;
import common.util.unit.Trait;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ForLoopReplaceableByForEach")
public class EUnit extends Entity {

	public static class OrbHandler extends BattleObj {
		protected static int getOrbAtk(AttackAb atk, EEnemy en) {
			if (atk.matk == null) {
				return 0;
			}

			if (atk.origin.model instanceof AtkModelUnit) {
				// Warning : Eunit.e became public now
				EUnit unit = (EUnit) ((AtkModelUnit) atk.origin.model).e;

				return unit.getOrbAtk(en.traits, atk.matk);
			}

			return 0;
		}

		protected static float getOrbMassive(AttackAb atk, ArrayList<Trait> traits, Treasure t) {
			if(atk.origin.model instanceof AtkModelUnit) {
				return ((EUnit) ((AtkModelUnit) atk.origin.model).e).getOrbMassive(atk.trait, traits, t);
			}

			return ((EUnit) ((AtkModelUnit)atk.model).e).getOrbMassive(atk.trait, traits, t);
		}

		protected static float getOrbGood(AttackAb atk, ArrayList<Trait> traits, Treasure t) {
			if(atk.origin.model instanceof AtkModelUnit) {
				return ((EUnit) ((AtkModelUnit) atk.origin.model).e).getOrbGood(atk.trait, traits, t);
			}

			return ((EUnit) ((AtkModelUnit)atk.model).e).getOrbGood(atk.trait, traits, t);
		}
	}

	public final int lvl;
	public final int[] index;

	protected final Level level;

	public final boolean isSpirit;

	public EUnit(StageBasis b, MaskUnit de, EAnimU ea, float d0, int layer0, int layer1, Level level, PCoin pc, int[] index, boolean isSpirit) {
		super(b, de, ea, d0, b.b.t().getAtkMulti(), b.b.t().getDefMulti(), pc, level);
		layer = layer0 == layer1 ? layer0 : layer0 + (int) (b.r.nextFloat() * (layer1 - layer0 + 1));
		traits = de.getTraits();
		lvl = level.getLv() + level.getPlusLv();
		this.index = index;

		this.level = level;

		this.isSpirit = isSpirit;
	}

	//used for waterblast
	public EUnit(StageBasis b, MaskUnit de, EAnimU ea, float d0) {
		super(b, de, ea, d0, b.b.t().getAtkMulti(), b.b.t().getDefMulti(), null, null);
		layer = de.getFront() + (int) (b.r.nextFloat() * (de.getBack() - de.getFront() + 1));
		traits = de.getTraits();
		this.index = null;

		lvl = 1;
		health = maxH = (int) (health * b.b.t().getCannonMagnification(BASE_WALL, BASE_WALL_MAGNIFICATION) / 100.0);
		level = null;
		isSpirit = false;
	}

	@Override
	public int getAtk() {
		int atk = aam.getAtk();
		if (status[P_STRONG][0] != 0)
			atk += atk * (status[P_STRONG][0] + basis.b.getInc(C_STRONG)) / 100;
		if (status[P_WEAK][0] > 0)
			atk = atk * status[P_WEAK][1] / 100;
		return atk;
	}

	@Override
	public void update() {
		super.update();

		traits = status[P_CURSE][0] == 0 && status[P_SEAL][0] == 0 ? data.getTraits() : new ArrayList<>();

		if (isSpirit && atkm.atkTime == 0) {
			kill(KillMode.SPIRIT);
		}
	}

	@Override
	public void added(int d, float p) {
		super.added(d, p);

		if (isSpirit) {
			atkm.setUp();
		}
	}

	@Override
	public void damaged(AttackAb atk) {
		if (isSpirit) {
			status[P_IMUATK][0] = Integer.MAX_VALUE;
			anim.getEff(P_IMUATK);

			return;
		}

		if (atk.trait.contains(BCTraits.get(TRAIT_BEAST))) {
			Proc.BSTHUNT beastDodge = getProc().BSTHUNT;

			if (beastDodge.prob > 0 && (atk.dire != dire)) {
				if (status[P_BSTHUNT][0] == 0 && (beastDodge.prob == 100 || basis.r.nextFloat() * 100 < beastDodge.prob)) {
					status[P_BSTHUNT][0] = beastDodge.time;
					anim.getEff(P_IMUATK);
				}

				if (status[P_BSTHUNT][0] > 0) {
					damageTaken += atk.atk;

					if(index != null) {
						basis.totalDamageTaken[index[0]][index[1]] += atk.atk;
					}

					return;
				}
			}
		}

		super.damaged(atk);

		if(index != null) {
			basis.totalDamageTaken[index[0]][index[1]] += atk.atk;
		}
	}

	@Override
	public float getResistValue(AttackAb atk, String procName, int procResist) {
		float ans = 1f - procResist / 100f;

		boolean canBeApplied = false;

		for (int i = 0; i < SUPER_SAGE_RESIST_TYPE.length; i++) {
			if (procName.equals(SUPER_SAGE_RESIST_TYPE[i])) {
				canBeApplied = true;

				break;
			}
		}

		if (atk.trait.contains(BCTraits.get(TRAIT_SAGE)) && canBeApplied && (getAbi() & AB_SKILL) != 0) {
			ans *= (1f - SUPER_SAGE_HUNTER_RESIST);
		}

		return ans;
	}

	@Override
	protected int getDamage(AttackAb atk, int ans) {
		if (atk instanceof AttackWave && atk.waveType == WT_MINI) {
			ans = (int) ((float) ans * atk.getProc().MINIWAVE.multi / 100.0);
		}

		if (atk instanceof AttackVolcano && atk.waveType == WT_MIVC) {
			ans = (int) ((float) ans * atk.getProc().MINIVOLC.mult / 100.0);
		}

		if (atk.model instanceof AtkModelEnemy && status[P_CURSE][0] == 0) {
			ArrayList<Trait> sharedTraits = new ArrayList<>(atk.trait);

			sharedTraits.retainAll(traits);

			boolean isAntiTraited = targetTraited(atk.trait);

			for (Trait t : traits) {
				if (t.BCTrait || sharedTraits.contains(t))
					continue;
				if ((t.targetType && isAntiTraited) || t.others.contains(((MaskUnit)data).getPack()))
					sharedTraits.add(t);
			}

			if ((getAbi() & AB_GOOD) != 0)
				ans = (int) (ans * basis.b.t().getGOODDEF(atk.trait, sharedTraits, ((MaskUnit)data).getOrb(), level));

			if ((getAbi() & AB_RESIST) != 0)
				ans = (int) (ans * basis.b.t().getRESISTDEF(atk.trait, sharedTraits, ((MaskUnit)data).getOrb(), level));

			if (!sharedTraits.isEmpty() && (getAbi() & AB_RESISTS) != 0)
				ans = (int) (ans * basis.b.t().getRESISTSDEF(sharedTraits));
		}

		if (atk.trait.contains(UserProfile.getBCData().traits.get(TRAIT_WITCH)) && (getAbi() & AB_WKILL) > 0)
			ans = (int) (ans * basis.b.t().getWKDef());

		if (atk.trait.contains(UserProfile.getBCData().traits.get(TRAIT_EVA)) && (getAbi() & AB_EKILL) > 0)
			ans = (int) (ans * basis.b.t().getEKDef());

		if (isBase)
			ans = (int) (ans * (1 + atk.getProc().ATKBASE.mult / 100.0));

		if (atk.trait.contains(UserProfile.getBCData().traits.get(TRAIT_BARON)) && (getAbi() & AB_BAKILL) > 0)
			ans = (int) (ans * 0.7);

		if (atk.trait.contains(UserProfile.getBCData().traits.get(Data.TRAIT_BEAST)) && getProc().BSTHUNT.type.active)
			ans = (int) (ans * 0.6);

		if (atk.trait.contains(UserProfile.getBCData().traits.get(Data.TRAIT_SAGE)) && (getAbi() & AB_SKILL) > 0)
			ans = (int) (ans * SUPER_SAGE_HUNTER_HP);

		// Perform orb
		ans = getOrbRes(atk.trait, ans);

		if(basis.canon.base > 0) {
			ans = (int) (ans * basis.b.t().getBaseMagnification(basis.canon.base, atk.trait));
		}

		ans = critCalc((getAbi() & AB_METALIC) != 0, ans, atk);

		return ans;
	}

	@Override
	protected float getLim() {
		return Math.max(0, basis.st.len - pos - ((MaskUnit) data).getLimit());
	}

	@Override
	protected int traitType() {
		return -1;
	}

	@Override
	protected void updateMove(float extmov) {
		extmov = (float) (data.getSpeed() * basis.b.getInc(C_SPE) / 50) / 4f;
		super.updateMove(extmov);
	}

	private int getOrbAtk(ArrayList<Trait> trait, MaskAtk matk) {
		Orb orb = ((MaskUnit) data).getOrb();

		if (orb == null || level.getOrbs() == null) {
			return 0;
		}

		int ans = 0;

		for (int[] line : level.getOrbs()) {
			if (line.length == 0)
				continue;

			if (line[ORB_TYPE] != Data.ORB_ATK)
				continue;

			List<Trait> orbType = Trait.convertOrb(line[ORB_TRAIT]);

			boolean orbValid = false;

			for(int i = 0; i < orbType.size(); i++) {
				if (trait.contains(orbType.get(i))) {
					orbValid = true;

					break;
				}
			}

			if (!orbValid)
				continue;

			ans += orb.getAtk(line[ORB_GRADE], matk);
		}

		return ans;
	}

	private int getOrbRes(ArrayList<Trait> trait, int atk) {
		Orb orb = ((MaskUnit) data).getOrb();

		if (orb == null || level.getOrbs() == null)
			return atk;

		int ans = atk;

		for (int[] line : level.getOrbs()) {
			if (line.length == 0 || line[ORB_TYPE] != Data.ORB_RES)
				continue;

			List<Trait> orbType = Trait.convertOrb(line[ORB_TRAIT]);

			boolean orbValid = false;

			for(int i = 0; i < orbType.size(); i++) {
				if (trait.contains(orbType.get(i))) {
					orbValid = true;

					break;
				}
			}

			if (!orbValid)
				continue;

			ans = orb.getRes(line[ORB_GRADE], ans);
		}

		return ans;
	}

	private float getOrbMassive(ArrayList<Trait> eTraits, ArrayList<Trait> traits, Treasure t) {
		float ini = 1;

		if (!traits.isEmpty())
			ini = 3 + 1f / 3 * t.getFruit(traits);

		Orb orbs = ((MaskUnit)data).getOrb();

		if(orbs != null && level.getOrbs() != null) {
			int[][] levelOrbs = level.getOrbs();

			for(int i = 0; i < levelOrbs.length; i++) {
				if (levelOrbs[i].length < ORB_TOT)
					continue;

				if (levelOrbs[i][ORB_TYPE] == ORB_MASSIVE) {
					List<Trait> orbType = Trait.convertOrb(levelOrbs[i][ORB_TRAIT]);

					for(int j = 0; j < orbType.size(); j++) {
						if (eTraits.contains(orbType.get(j))) {
							ini += ORB_MASSIVE_MULTI[levelOrbs[i][ORB_GRADE]];

							break;
						}
					}
				}
			}
		}

		if (ini == 1)
			return ini;

		float com = 1 + t.b.getInc(C_MASSIVE) * 0.01f;

		return ini * com;
	}

	private float getOrbGood(ArrayList<Trait> eTraits, ArrayList<Trait> traits, Treasure t) {
		float ini = 1;

		if (!traits.isEmpty())
			ini = 1.5f * (1 + 0.2f / 3 * t.getFruit(traits));

		Orb orbs = ((MaskUnit)data).getOrb();

		if(orbs != null && level.getOrbs() != null) {
			int[][] levelOrbs = level.getOrbs();

			for (int i = 0; i < levelOrbs.length; i++) {
				if (levelOrbs[i].length < ORB_TOT)
						continue;

				if (levelOrbs[i][ORB_TYPE] == ORB_STRONG) {
					List<Trait> orbType = Trait.convertOrb(levelOrbs[i][ORB_TRAIT]);

					for(int j = 0; j < orbType.size(); j++) {
						if (eTraits.contains(orbType.get(j))) {
							ini += ORB_STR_ATK_MULTI[levelOrbs[i][ORB_GRADE]];

							break;
						}
					}
				}
			}
		}

		if (ini == 1)
			return ini;

		float com = 1 + t.b.getInc(C_GOOD) * 0.01f;
		return ini * com;
	}

	@Override
	protected void onLastBreathe() {
		basis.notifyUnitDeath();
	}
}
