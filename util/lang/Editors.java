package common.util.lang;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Consumer;

import org.jcodec.common.tools.MathUtil;

import common.pack.UserProfile;
import common.util.Data;
import common.util.Data.Proc;
import common.util.Data.Proc.ProcItem;
import common.util.lang.LocaleCenter.Displayable;
import common.util.lang.ProcLang;
import common.util.lang.ProcLang.ItemLang;

public class Editors {

	public static class DispItem implements Displayable {

		private final ProcLang.ItemLang lang;
		private final Proc.ProcItem proc;
		private final Formatter.Context ctx;

		public DispItem(ProcLang.ItemLang lang, Proc.ProcItem proc, Formatter.Context ctx) {
			this.lang = lang;
			this.proc = proc;
			this.ctx = ctx;
		}

		@Override
		public String getName() {
			return lang.full_name;
		}

		@Override
		public String getTooltip() {
			return (lang.tooltip == null ? "" : lang.tooltip + "<hr>") + Formatter.format(lang.format, proc, ctx);
		}

		@Override
		public void setName(String str) {
			lang.full_name = str;
		}

		@Override
		public void setTooltip(String str) {
			lang.tooltip = str;
		}

	}

	public static class EditControl<T> {

		public final Class<T> cls;
		private final Consumer<T> regulator;

		public EditControl(Class<T> cls, Consumer<T> func) {
			this.cls = cls;
			regulator = func;
		}

		public Field getField(String f) {
			return Data.err(() -> {
				if (f.contains(".")) {
					String[] strs = f.split("\\.");
					return cls.getField(strs[0]).getType().getField(strs[1]);
				} else
					return cls.getField(f);
			});
		}

		@SuppressWarnings("unchecked")
		public final void update(EditorGroup par) {
			regulate((T) par.obj);
			par.setData(par.obj);
			if (par.callback != null)
				par.callback.run();
		}

		protected void regulate(T obj) {
			regulator.accept(obj);
		}

	}

	public static abstract class Editor {

		public final EditorGroup par;
		public final Field field;

		public Editor(EditorGroup par, Field field, String f) throws Exception {
			this.par = par;
			this.field = field;
		}

		/** notify that the data changed */
		protected abstract void setData();

		protected final void update() {
			par.ctrl.update(par);
		}

	}

	public static class EditorGroup {

		public final String proc; // Proc Title
		public final Class<?> cls; // ProcItem
		public final Editor[] list;
		public final EditControl<?> ctrl;
		public final Runnable callback;

		public Object obj;

		public EditorGroup(String proc, boolean edit, Runnable cb) {
			this.proc = proc;
			this.cls = Data.err(() -> Proc.class.getDeclaredField(proc)).getType();
			this.callback = cb;
			ctrl = map().get(proc);
			ItemLang item = ProcLang.get().get(proc);
			String[] arr = item.list();
			list = new Editor[arr.length];
			for (int i = 0; i < arr.length; i++) {
				list[i] = getEditor(ctrl, this, arr[i], edit);
			}
		}

		public LocaleCenter.Binder getItem(Formatter.Context ctx) {
			ProcLang.ItemLang lang = ProcLang.get().get(proc);
			Displayable disp = new DispItem(lang, (ProcItem) obj, ctx);
			return new LocaleCenter.ObjBinder(disp, proc, (name) -> getItem(ctx));
		}

		public void setData(Object obj) {
			this.obj = obj;
			for (Editor e : list)
				e.setData();
		}

	}

	public static interface EditorSupplier {

		public Editor getEditor(EditControl<?> ctrl, EditorGroup g, String field, boolean edit);

	}

	static {
		EditControl<Proc.PROB> prob = new EditControl<>(Proc.PROB.class, (t) -> {
			t.prob = MathUtil.clip(t.prob, 0, 100);
		});

		EditControl<Proc.PT> pt = new EditControl<>(Proc.PT.class, (t) -> {
			t.prob = MathUtil.clip(t.prob, 0, 100);
			if (t.prob == 0)
				t.time = 0;
			else
				t.time = Math.max(t.time, 1);
		});

		map().put("KB", new EditControl<>(Proc.PTD.class, (t) -> {
			t.prob = MathUtil.clip(t.prob, 0, 100);
			if (t.prob == 0) {
				t.dis = t.time = 0;
			} else {
				if (t.dis == 0)
					t.dis = Data.KB_DIS[Data.INT_KB];
				if (t.time <= 0)
					t.time = Data.KB_TIME[Data.INT_KB];
			}
		}));

		map().put("STOP", pt);

		map().put("SLOW", pt);

		map().put("CRIT", new EditControl<>(Proc.PM.class, (t) -> {
			t.prob = MathUtil.clip(t.prob, 0, 100);
			if (t.prob == 0)
				t.mult = 0;
			else if (t.mult == 0)
				t.mult = 200;
		}));

		map().put("WAVE", new EditControl<>(Proc.WAVE.class, (t) -> {
			t.prob = MathUtil.clip(t.prob, 0, 100);
			if (t.prob == 0)
				t.lv = 0;
			t.lv = MathUtil.clip(t.lv, 1, 20);
		}));

		map().put("WEAK", new EditControl<>(Proc.WEAK.class, (t) -> {
			t.prob = MathUtil.clip(t.prob, 0, 100);
			if (t.prob == 0)
				t.mult = t.time = 0;
			else {
				t.time = Math.max(t.time, 1);
			}
		}));

		map().put("BREAK", prob);

		map().put("WARP", new EditControl<>(Proc.PTD.class, (t) -> {
			t.prob = MathUtil.clip(t.prob, 0, 100);
			if (t.prob == 0) {
				t.dis = t.time = 0;
			}
		}));

		map().put("CURSE", pt);

		map().put("STRONG", new EditControl<>(Proc.STRONG.class, (t) -> {
			t.health = MathUtil.clip(t.health, 0, 99);
			if (t.health == 0)
				t.mult = 0;
		}));

		map().put("LETHAL", prob);

		map().put("BURROW", new EditControl<>(Proc.BURROW.class, (t) -> {
			t.count = Math.max(t.count, -1);
			if (t.count == 0)
				t.dis = 0;
			else
				t.dis = Math.max(t.dis, 1);
		}));

		map().put("REVIVE", new EditControl<>(Proc.REVIVE.class, (t) -> {
			t.count = Math.max(t.count, -1);
			if (t.count == 0) {
				t.health = 0;
				t.time = 0;
				t.type.imu_zkill = false;
				t.type.revive_others = false;
				t.dis_0 = t.dis_1 = 0;
				t.type.range_type = 0;
				t.type.revive_non_zombie = false;
			} else {
				t.health = MathUtil.clip(t.health, 1, 100);
				t.time = Math.max(t.time, 1);
				if (!t.type.revive_others) {
					t.dis_0 = t.dis_1 = 0;
					t.type.range_type = 0;
					t.type.revive_non_zombie = false;
				} else {
					t.type.range_type = MathUtil.clip(t.type.range_type, 0, 3);
				}
			}
		}));

		map().put("SNIPER", prob);

		map().put("TIME", pt);

		map().put("SEAL", pt);

		map().put("SUMMON", new EditControl<>(Proc.SUMMON.class, (t) -> {
			t.prob = MathUtil.clip(t.prob, 0, 100);
			if (t.prob == 0) {
				t.dis = 0;
				t.id = null;
				t.mult = 0;
				t.time = 0;
				t.type.anim_type = 0;
				t.type.fix_buff = false;
				t.type.ignore_limit = false;
				t.type.on_hit = false;
				t.type.on_kill = false;
				t.type.random_layer = false;
				t.type.same_health = false;
			} else {
				t.mult = Math.max(1, t.mult);
				t.time = Math.max(0, t.time);
				t.type.anim_type = MathUtil.clip(t.type.anim_type, 0, 3);
			}
		}));

		map().put("MOVEWAVE", new EditControl<>(Proc.MOVEWAVE.class, (t) -> {
			t.prob = MathUtil.clip(t.prob, 0, 100);
			if (t.prob == 0) {
				t.dis = 0;
				t.itv = 0;
				t.speed = 0;
				t.time = 0;
				t.width = 0;
			} else {
				t.width = Math.max(0, t.width);
				t.time = Math.max(1, t.time);
				t.itv = Math.max(1, t.itv);
			}
		}));

		map().put("THEME", new EditControl<>(Proc.THEME.class, (t) -> {
			t.prob = MathUtil.clip(t.prob, 0, 100);
		}));

		map().put("POISON", new EditControl<>(Proc.POISON.class, (t) -> {
			t.prob = MathUtil.clip(t.prob, 0, 100);
			if (t.prob == 0) {
				t.damage = 0;
				t.itv = 0;
				t.time = 0;
				t.type.damage_type = 0;
				t.type.unstackable = false;
			} else {
				t.time = Math.max(1, t.time);
				t.itv = Math.max(1, t.itv);
				t.type.damage_type = MathUtil.clip(t.type.damage_type, 0, 3);
			}
		}));

		map().put("BOSS", prob);

		map().put("CRITI", new EditControl<>(Proc.CRITI.class, (t) -> {
			t.type = MathUtil.clip(0, t.type, 2);
		}));

		map().put("SATK", new EditControl<>(Proc.PM.class, (t) -> {
			if (t.prob == 0)
				t.mult = 0;
		}));

		map().put("IMUATK", pt);

		map().put("POIATK", new EditControl<>(Proc.PM.class, (t) -> {
			if (t.prob == 0)
				t.mult = 0;
		}));

		map().put("VOLC", new EditControl<>(Proc.VOLC.class, (t) -> {
			t.prob = MathUtil.clip(t.prob, 0, 100);
			if (t.prob == 0) {
				t.dis_0 = t.dis_1 = 0;
				t.time = 0;
			} else {
				t.time = Math.max(1, t.time / Data.VOLC_ITV) * Data.VOLC_ITV;
			}
		}));

		map().put("ARMOR", new EditControl<>(Proc.ARMOR.class, (t) -> {
			t.prob = MathUtil.clip(t.prob, 0, 100);
			if (t.prob == 0) {
				t.mult = t.time = 0;
			} else {
				t.time = Math.max(1, t.time);
			}
		}));

		map().put("SPEED", new EditControl<>(Proc.SPEED.class, (t) -> {
			t.prob = MathUtil.clip(t.prob, 0, 100);
			if (t.prob == 0) {
				t.speed = t.time = 0;
				t.type = 0;
			} else {
				t.time = Math.max(1, t.time);
				t.type = MathUtil.clip(t.type, 0, 2);
			}
		}));

	}

	public static void setEditorSupplier(EditorSupplier sup) {
		UserProfile.setStatic("Editor_Supplier", sup);
	}

	private static Editor getEditor(EditControl<?> ctrl, EditorGroup g, String field, boolean edit) {
		EditorSupplier edi = UserProfile.getStatic("Editor_Supplier", () -> null);
		return edi.getEditor(ctrl, g, field, edit);
	}

	@SuppressWarnings("rawtypes")
	private static Map<String, EditControl> map() {
		return UserProfile.getRegister("Editor_EditControl", EditControl.class);
	}

}
