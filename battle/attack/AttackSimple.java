package common.battle.attack;

import common.battle.data.MaskAtk;
import common.battle.entity.AbEntity;
import common.battle.entity.Entity;
import common.battle.entity.Sniper;
import common.util.Data;
import common.util.Data.Proc.MOVEWAVE;
import common.util.Data.Proc.VOLC;
import common.util.unit.Trait;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AttackSimple extends AttackAb {

	/**
	 * avoid attacking already attacked enemies for lasting attacks
	 */
	private final Set<AbEntity> attacked = new HashSet<>();
	private final boolean range;
	public int ind = 0;

	public AttackSimple(Entity attacker, AtkModelAb ent, int ATK, ArrayList<Trait> tr, int eab, Proc pro, float p0, float p1, boolean isr,
						MaskAtk matk, int layer, boolean isLongAtk, int duration) {
		super(attacker, ent, ATK, tr, eab, pro, p0, p1, matk, layer, isLongAtk, duration);
		range = isr;
	}

	public AttackSimple(Entity attacker, AtkModelAb ent, int ATK, ArrayList<Trait> tr, int eab, Proc proc, float p0, float p1, MaskAtk mask, int layer, boolean isLongAtk) {
		this(attacker, ent, ATK, tr, eab, proc, p0, p1, mask.isRange(), mask, layer, isLongAtk, 1);
		touch = mask.getTarget();

		if((eab & AB_CKILL) > 0)
			touch |= TCH_CORPSE;

		dire *= mask.getDire();
	}

	public AttackSimple(Entity attacker, AtkModelAb ent, int ATK, ArrayList<Trait> tr, int eab, Proc proc, float p0, float p1, MaskAtk mask, int layer, boolean isLongAtk, int ind) {
		this(attacker, ent, ATK, tr, eab, proc, p0, p1, mask, layer, isLongAtk);
		this.ind = ind;
	}

	@Override
	public void capture() {
		float pos = model.getPos();
		List<AbEntity> le = model.b.inRange(touch, dire, sta, end, excludeLastEdge);
		if(attacker != null && isLongAtk && !le.contains(model.b.getBase(attacker.dire))) {
			if(attacker.dire == -1 && dire == -1 && sta <= model.b.getBase(attacker.dire).pos)
				le.add(model.b.getBase(attacker.dire));
			else if (attacker.dire == 1 && dire == 1 && sta >= model.b.getBase(attacker.dire).pos)
				le.add(model.b.getBase(attacker.dire));
		}
		le.removeIf(attacked::contains);
		capt.clear();
		if (canon > -2 || model instanceof Sniper)
			le.remove(model.b.ebase);
		if ((abi & AB_ONLY) == 0)
			capt.addAll(le);
		else
			for (AbEntity e : le)
				if (e.ctargetable(trait, attacker, true))
					capt.add(e);
		if (!range) {
			if (capt.isEmpty())
				return;

			List<AbEntity> ents = new ArrayList<>();
			ents.add(capt.get(0));

			if (dire == 1) {

				double leftMost = ents.get(0).pos;

				for (AbEntity e: capt) {
					if (e.pos < leftMost) {
						leftMost = e.pos;
						ents.clear();
						ents.add(e);
					} else if (e.pos == leftMost) {
						ents.add(e);
					}
				}

			} else {

				double rightMost = ents.get(0).pos;

				for (AbEntity e: capt) {
					if (e.pos > rightMost) {
						rightMost = e.pos;
						ents.clear();
						ents.add(e);
					} else if (e.pos == rightMost) {
						ents.add(e);
					}
				}

			}

			capt.clear();
			int r = (int) (model.b.r.nextFloat() * ents.size());
			capt.add(ents.get(r));
		}
	}

	/**
	 * Method to manually add a unit to an attack for counters.
	 */
	public boolean counterEntity(Entity ce) {
		isCounter = true;
		if (ce != null && !capt.contains(ce))
			capt.add(ce);
		excuse();
		return !capt.isEmpty();
	}

	@Override
	public void excuse() {
		process();

		//At this point, attacker must not be null if attack is sent from entity
		//Thus we keep real "raw" attack, and change value such as weaken/strengthen here
		if (attacker != null) {
			if (attacker.status[P_STRONG][0] != 0)
				atk += atk * attacker.status[P_STRONG][0] / 100;
			if (attacker.status[P_WEAK][0] != 0)
				atk = atk * attacker.status[P_WEAK][1] / 100;
		}

		int layer = model.getLayer();
		if (proc.MOVEWAVE.exists()) {
			MOVEWAVE mw = proc.MOVEWAVE;
			int dire = model.getDire();
			float p0 = model.getPos() + dire * mw.dis;
			new ContMove(this, p0, mw.width, mw.speed, 1, mw.time, mw.itv, layer);
			return;
		}
		for (AbEntity e : capt) {
			e.damaged(this);
			attacked.add(e);
		}
		if (!capt.isEmpty() && proc.WAVE.exists()) {
			int dire = model.getDire();
			int wid = dire == 1 ? W_E_WID : W_U_WID;
			float addp = (dire == 1 ? W_E_INI : W_U_INI) + wid / 2f;
			float p0 = model.getPos() + dire * addp;
			// generate a wave when hits somebody

			ContWaveDef wave = new ContWaveDef(new AttackWave(attacker, this, p0, wid, WT_WAVE), p0, layer, -3);

			if(attacker != null) {
				attacker.summoned.add(wave);
			}
		}

		if(!capt.isEmpty() && proc.MINIWAVE.exists()) {
			int dire = model.getDire();
			int wid = dire == 1 ? W_E_WID : W_U_WID;
			float addp = (dire == 1 ? W_E_INI : W_U_INI) + wid / 2f;
			float p0 = model.getPos() + dire * addp;

			ContWaveDef wave = new ContWaveDef(new AttackWave(attacker, this, p0, wid, WT_MINI), p0, layer, -1);

			if(attacker != null) {
				attacker.summoned.add(wave);
			}
		}

		if (!capt.isEmpty() && proc.VOLC.exists()) {
			int dire = model.getDire();
			VOLC volc = proc.VOLC;
			int addp = volc.dis_0 + (int) (model.b.r.nextFloat() * (volc.dis_1 - volc.dis_0));
			float p0 = model.getPos() + dire * addp;
			float sta = p0 + (dire == 1 ? W_VOLC_PIERCE : W_VOLC_INNER);
			float end = p0 - (dire == 1 ? W_VOLC_INNER : W_VOLC_PIERCE);

			ContVolcano volcano = new ContVolcano(new AttackVolcano(attacker, this, sta, end, Data.WT_VOLC), p0, layer, volc.time, volc.dis_0, volc.dis_1, ind);

			if(attacker != null) {
				attacker.summoned.add(volcano);
			}
		}

		if (!capt.isEmpty() && proc.MINIVOLC.exists()) {
			int dire = model.getDire();
			Proc.MINIVOLC volc = proc.MINIVOLC;
			int addp = volc.dis_0 + (int) (model.b.r.nextFloat() * (volc.dis_1 - volc.dis_0));
			float p0 = model.getPos() + dire * addp;
			float sta = p0 + (dire == 1 ? W_VOLC_PIERCE : W_VOLC_INNER);
			float end = p0 - (dire == 1 ? W_VOLC_INNER : W_VOLC_PIERCE);

			ContVolcano volcano = new ContVolcano(new AttackVolcano(attacker, this, sta, end, Data.WT_MIVC), p0, layer, volc.time, volc.dis_0, volc.dis_1, ind);

			if(attacker != null) {
				attacker.summoned.add(volcano);
			}
		}
	}

}
