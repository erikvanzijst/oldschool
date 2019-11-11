package cx.prutser.xmlmath.operands;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import cx.prutser.xmlmath.schemas.StanzaType;

/**
 * 
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	v1.0, Apr 11, 2006
 */
public final class Scope {

	private static final ThreadLocal<Stack<Scope>> tlocal =
		new ThreadLocal<Stack<Scope>>();
	
	private final Scope parent;
	private final Map<String, Declaration> vars =
		new HashMap<String, Declaration>();
	private final Map<String, StanzaType> stanzaTypes =
		new HashMap<String, StanzaType>();
	
	private Scope(Scope parent) {
		this.parent = parent;
	}
	
	public void setDeclaration(Declaration var) {
		
		vars.put(var.getName(), var);
	}
	
	/**
	 * Returns a reference to the declaration ojbect that defines the variable
	 * with the specified name. The search for the declaration starts in the
	 * current scope. If the declaration is not found in a scope, the search
	 * continues in the parent scope until a declaration with the given name
	 * is found.<br/>
	 * If the declaration was not found, <tt>null</null> is returned.
	 * 
	 * @param name
	 * @return
	 */
	public Declaration getDeclaration(String name) {

		Declaration var = vars.get(name);
		
		if(var == null && parent != null) {
			var = parent.getDeclaration(name);
		}
		return var;
	}
	
	public void setStanzaType(StanzaType stanza) {
		
		stanzaTypes.put(stanza.getName(), stanza);
	}
	
	public StanzaType getStanzaType(String name) {
		
		StanzaType stanza = stanzaTypes.get(name);
		
		if(stanza == null && parent != null) {
			stanza = parent.getStanzaType(name);
		}
		return stanza;
	}
	
	private static Stack<Scope> getStack() {
		
		Stack<Scope> stack = tlocal.get();
		if(stack == null) {
			tlocal.set(stack = new Stack<Scope>());
		}
		return stack;
	}
	
	/**
	 * Creates a new scope instance with the parent scope set to the most
	 * recently pushed scope instance.
	 * 
	 * @return
	 */
	public static Scope createScope() {
		
		return new Scope(getScope());
	}
	
	public static void push(Scope scope) {
		
		getStack().push(scope);
	}
	
	public static Scope pop() throws EmptyStackException {
		
		return getStack().pop();
	}

	/**
	 * Returns the current scope for this thread. Returns <tt>null</tt> if
	 * this thread has not yet opened a scope.
	 * 
	 * @return
	 */
	public static Scope getScope() {
		
		Stack<Scope> stack = getStack();
		return stack.isEmpty() ? null : stack.peek();
	}
}
