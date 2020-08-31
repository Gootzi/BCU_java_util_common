package common.io.json;

import com.google.gson.*;
import common.io.json.JsonClass.JCGeneric;
import common.io.json.JsonClass.JCIdentifier;
import common.io.json.JsonException.Type;
import common.pack.Identifier;
import common.util.Data;

import java.lang.reflect.*;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Dependency {

	private static class DependencyCheck {

		protected static void collect(Dependency set, Object obj, DependencyCheck par) throws Exception {
			if (obj == null)
				return;
			if (obj instanceof JsonElement)
				return;
			if (obj instanceof Number)
				return;
			if (obj instanceof Boolean)
				return;
			if (obj instanceof String)
				return;
			if (obj instanceof Class)
				return;
			if (obj instanceof Identifier) {
				set.add((Identifier<?>) obj);
				return;
			}
			Class<?> cls = obj.getClass();
			if (cls.isArray()) {
				int n = Array.getLength(obj);
				for (int i = 0; i < n; i++)
					collect(set, Array.get(obj, i), par);
				return;
			}
			if (cls.getAnnotation(JCGeneric.class) != null && par != null && par.curjfld.alias().length > par.index) {
				JCGeneric jcg = cls.getAnnotation(JCGeneric.class);
				Class<?> alias = par.curjfld.alias()[par.index];
				boolean found = false;
				for (Class<?> ala : jcg.value())
					if (ala == alias) {
						found = true;
						break;
					}
				if (!found)
					throw new JsonException(Type.TYPE_MISMATCH, null, "class not present in JCGeneric");
				for (Field f : cls.getFields()) {
					JCIdentifier jcgw = f.getAnnotation(JCIdentifier.class);
					if (jcgw != null && f.getType() == alias) {
						collect(set, f.get(obj), par);
						return;
					}
				}
				Constructor<?> con = alias.getConstructor(cls);
				collect(set, con.newInstance(obj), par);
				return;
			}
			if (par != null && par.curjfld != null) {
				JsonField jfield = par.curjfld;
				if (jfield.ser() == JsonField.SerType.FUNC) {
					if (jfield.serializer().length() == 0)
						throw new JsonException(Type.FUNC, null, "no serializer function");
					Method m = par.obj.getClass().getMethod(jfield.serializer(), cls);
					collect(set, m.invoke(par.obj, obj), null);
					return;
				} else if (jfield.ser() == JsonField.SerType.CLASS) {
					JsonClass cjc = cls.getAnnotation(JsonClass.class);
					if (cjc == null || cjc.serializer().length() == 0)
						throw new JsonException(Type.FUNC, null, "no serializer function");
					String func = cjc.serializer();
					Method m = cls.getMethod(func);
					collect(set, m.invoke(obj), null);
					return;
				}
			}
			JsonClass jc = cls.getAnnotation(JsonClass.class);
			if (jc != null)
				if (jc.write() == JsonClass.WType.DEF) {
					new DependencyCheck(set, par, obj);
					return;
				} else if (jc.write() == JsonClass.WType.CLASS) {
					if (jc.serializer().length() == 0)
						throw new JsonException(Type.FUNC, null, "no serializer function");
					String func = jc.serializer();
					Method m = cls.getMethod(func);
					collect(set, m.invoke(obj), null);
					return;
				}
			if (obj instanceof List) {
				for (Object o : (List<?>) obj)
					collect(set, o, par);
				return;
			}
			if (obj instanceof Set) {
				for (Object o : (Set<?>) obj)
					collect(set, o, par);
				return;
			}
			if (obj instanceof Map) {
				for (Entry<?, ?> ent : ((Map<?, ?>) obj).entrySet()) {
					collect(set, ent.getKey(), par);
					collect(set, ent.getValue(), par);
				}
				return;
			}
			throw new JsonException(Type.UNDEFINED, null, "object " + obj + ":" + obj.getClass() + " not defined");
		}

		private final DependencyCheck par;
		private final Object obj;
		private final Dependency set;

		private JsonClass curjcls;
		private JsonField curjfld;
		private int index = 0;

		private DependencyCheck(Dependency set, DependencyCheck parent, Object object) throws Exception {
			this.set = set;
			par = parent;
			obj = object;
			collect(obj.getClass());
		}

		private void collect(Class<?> cls) throws Exception {
			if (cls.getSuperclass().getAnnotation(JsonClass.class) != null)
				collect(cls.getSuperclass());
			curjcls = cls.getAnnotation(JsonClass.class);
			for (Field f : cls.getDeclaredFields())
				if (curjcls.noTag() == JsonClass.NoTag.LOAD || f.getAnnotation(JsonField.class) != null) {
					if (Modifier.isStatic(f.getModifiers()))
						continue;
					JsonField jf = f.getAnnotation(JsonField.class);
					if (jf == null)
						jf = JsonField.DEF;
					if (jf.block() || jf.io() == JsonField.IOType.R)
						continue;
					f.setAccessible(true);
					curjfld = jf;
					Object val = f.get(obj);
					collect(set, val, getInvoker());
					curjfld = null;
				}
			for (Method m : cls.getDeclaredMethods())
				if (m.getAnnotation(JsonField.class) != null) {
					JsonField jf = m.getAnnotation(JsonField.class);
					if (jf.io() == JsonField.IOType.R)
						continue;
					if (jf.io() == JsonField.IOType.RW)
						throw new JsonException(Type.FUNC, null, "functional fields should not have RW type");
					String tag = jf.tag();
					if (tag.length() == 0)
						throw new JsonException(Type.TAG, null, "function fields must have tag");
					curjfld = jf;
					collect(set, m.invoke(obj), getInvoker());
					curjfld = null;
				}
		}

		private DependencyCheck getInvoker() {
			return curjcls.bypass() ? par : this;
		}

	}

	public static Dependency collect(Object obj) {
		Dependency set = new Dependency();
		Data.err(() -> DependencyCheck.collect(set, obj, null));
		return set;
	}

	public Map<String, Map<Class<?>, Set<Identifier<?>>>> map = new TreeMap<>();

	protected void add(Identifier<?> id) {
		Map<Class<?>, Set<Identifier<?>>> cont = null;
		if (map.containsKey(id.pack))
			cont = map.get(id.pack);
		else
			map.put(id.pack, cont = new TreeMap<>());
		Set<Identifier<?>> set = null;
		if (cont.containsKey(id.cls))
			set = cont.get(id.cls);
		else
			cont.put(id.cls, set = new TreeSet<>());
		set.add(id);

	}

}
