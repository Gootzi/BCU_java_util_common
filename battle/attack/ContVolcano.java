package common.battle.attack;

import common.CommonStatic;
import common.CommonStatic.BattleConst;
import common.battle.entity.AbEntity;
import common.system.P;
import common.system.fake.FakeGraphics;
import common.system.fake.FakeTransform;
import common.util.anim.EAnimD;
import common.util.pack.EffAnim.VolcEff;

import java.util.ArrayList;
import java.util.List;

public class ContVolcano extends ContAb {
	public final List<AbEntity> surgeSummoned = new ArrayList<>();

	public final int time, startPoint, endPoint;
	public final boolean reflected;

	protected final EAnimD<VolcEff> anim;
	protected final AttackVolcano v;
	private final Proc defProc;

	private final int aliveTime;

	private int t = 0;
	private final int ind;
	private final boolean[] performed = new boolean[4]; // [0,1] - check if curse/seal rng has passed, [2,3] - check if unit process needs to be updated

	//For counter surge
	public ContVolcano(AttackVolcano v, float p, int lay, int alive, int ind) {
		super(v.model.b, p, lay);

		if(v.waveType == WT_VOLC) {
			anim = (v.dire == 1 ? effas().A_E_VOLC : effas().A_VOLC).getEAnim(VolcEff.START);
		} else {
			anim = (v.dire == 1 ? effas().A_E_MINIVOLC : effas().A_MINIVOLC).getEAnim(VolcEff.START);
		}

		this.v = v;
		this.v.handler = this;

		aliveTime = alive;

		time = aliveTime;

		this.startPoint = 0;
		this.endPoint = 0;

		defProc = v.getProc().clone();

		this.ind = ind;

		this.reflected = true;

		CommonStatic.setSE(SE_VOLC_START);

		performed[0] = performed[2] = v.attacker.status[P_CURSE][0] == 0;
		performed[1] = performed[3] = v.attacker.status[P_SEAL][0] == 0;

		update();
	}

	protected ContVolcano(AttackVolcano v, float p, int lay, int alive, int startPoint, int endPoint, int ind) {
		super(v.model.b, p, lay);

		if(v.waveType == WT_VOLC) {
			anim = (v.dire == 1 ? effas().A_E_VOLC : effas().A_VOLC).getEAnim(VolcEff.START);
		} else {
			anim = (v.dire == 1 ? effas().A_E_MINIVOLC : effas().A_MINIVOLC).getEAnim(VolcEff.START);
		}

		this.v = v;
		this.v.handler = this;

		aliveTime = alive;

		time = aliveTime;

		this.startPoint = startPoint;
		this.endPoint = endPoint;

		defProc = v.getProc().clone();

		this.ind = ind;

		this.reflected = false;

		CommonStatic.setSE(SE_VOLC_START);

		performed[0] = performed[2] = v.attacker.status[P_CURSE][0] == 0;
		performed[1] = performed[3] = v.attacker.status[P_SEAL][0] == 0;

		update();
	}

	@Override
	public void draw(FakeGraphics gra, P p, float psiz) {
		FakeTransform at = gra.getTransform();
		anim.draw(gra, p, psiz);
		gra.setTransform(at);
		drawAxis(gra, p, psiz);
	}

	@Override
	public void update() {
		updateProc();
		if (t >= VOLC_PRE && t <= VOLC_PRE + aliveTime && anim.type != VolcEff.DURING) {
			anim.changeAnim(VolcEff.DURING, false);
			CommonStatic.setSE(SE_VOLC_LOOP);
		} else if (t > VOLC_PRE + aliveTime && anim.type != VolcEff.END)
			anim.changeAnim(VolcEff.END, false);

		if (t >= VOLC_PRE && t < VOLC_PRE + aliveTime && (t - VOLC_PRE) % VOLC_SE == 0) {
			CommonStatic.setSE(SE_VOLC_LOOP);
		}

		if (t >= aliveTime + VOLC_POST + VOLC_PRE) {
			activate = false;
		} else {
			t++;
			if (t >= VOLC_PRE && t <= VOLC_PRE + aliveTime)
				sb.getAttack(v);
			anim.update(false);
		}
	}

	@Override
	public void updateAnimation() {
		anim.update(false);
	}

	private void updateProc() {
		if (v.attacker.anim.dead == 0) {
			if (v.attacker.status[P_CURSE][0] > 0)
				v.attacker.status[P_CURSE][0]--;
			if (v.attacker.status[P_SEAL][0] > 0)
				v.attacker.status[P_SEAL][0]--;
		}

		String[] sealp = { "CRIT", "SNIPER", "BREAK", "SUMMON", "SATK", "SHIELDBREAK"};
		if (v.attacker.status[P_SEAL][0] > 0 && performed[3]) {
			performed[3] = false;
			for (String s : sealp)
				v.proc.get(s).clear();
		} else if (v.attacker.status[P_SEAL][0] == 0 && !performed[3]) {
			AtkModelEntity aam = (AtkModelEntity) v.model;
			for (String s : sealp)
				if (!v.proc.get(s).exists() && (defProc.get(s).exists() || (!performed[1] && aam.getProc(ind).get(s).perform(aam.b.r)))) {
					defProc.get(s).set(aam.getProc(ind).get(s));
					v.proc.get(s).set(aam.getProc(ind).get(s));
				}
			performed[1] = performed[3] = true;
		}
		String[] cursep = {"KB", "STOP", "SLOW", "WEAK", "WARP", "CURSE", "SNIPER", "SEAL", "POISON", "BOSS", "POIATK", "ARMOR", "SPEED", "DMGCUT", "DMGCAP"};
		if (v.attacker.status[P_CURSE][0] > 0 || v.attacker.status[P_SEAL][0] > 0 && performed[2]) {
			performed[2] = false;
			for (String s : cursep)
				v.proc.get(s).clear();
		} else if (v.attacker.status[P_CURSE][0] == 0 && v.attacker.status[P_SEAL][0] == 0 && !performed[2]) {
			AtkModelEntity aam = (AtkModelEntity) v.model;
			for (String s : cursep)
				if (!v.proc.get(s).exists() && (defProc.get(s).exists() || (!performed[0] && aam.getProc(ind).get(s).perform(aam.b.r)))) {
					defProc.get(s).set(aam.getProc(ind).get(s));
					v.proc.get(s).set(aam.getProc(ind).get(s));
				}
			performed[0] = performed[2] = true;
		}
	}

	protected void drawAxis(FakeGraphics gra, P p, float siz) {
		if (!CommonStatic.getConfig().ref)
			return;

		// after this is the drawing of hit boxes
		siz *= 1.25;
		float rat = BattleConst.ratio;
		int h = (int) (640 * rat * siz);
		gra.setColor(FakeGraphics.MAGENTA);
		float d0 = Math.min(v.sta, v.end);
		float ra = Math.abs(v.sta - v.end);
		int x = (int) ((d0 - pos) * rat * siz + p.x);
		int y = (int) p.y;
		int w = (int) (ra * rat * siz);

		if (v.attacked) {
			gra.fillRect(x, y, w, h);
			v.attacked = !v.attacked;
		} else {
			gra.drawRect(x, y, w, h);
		}
	}

	@Override
	public boolean IMUTime() {
		return (v.attacker.getAbi() & AB_TIMEI) != 0;
	}
}
