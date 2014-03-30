package thinlet;

import java.lang.reflect.*;

public class Listener {
	
	private Object target;
	private Method method;
	private Listener next;
	
	public Listener(Object target, String method) {
		this.target = target;
		try {
			this.method = target.getClass().getMethod(method);
		} catch (Exception exc) { throw new IllegalArgumentException(exc.getMessage()); }
	}
	
	private void invoke() throws Exception {
		method.invoke(target);
	}
	
	static Listener add(Listener first, Listener listener) {
		if (first == null) return listener;
		Listener last = first; while (last.next != null) last = last.next;
		last.next = listener;
		return first;
	}
	
	static Listener remove(Listener first, Listener listener) {
		if (first == listener) return first.next;
		Listener prev = first; while (prev.next != listener) prev = prev.next;
		prev.next = listener.next;
		return first;
	}
	
	static void invoke(Listener first) {
		for (Listener listener = first; listener != null; listener = listener.next) {
			try {
				listener.invoke();
			} catch (Exception exc) { throw new IllegalArgumentException(exc.getMessage()); }
		}
	}
}
