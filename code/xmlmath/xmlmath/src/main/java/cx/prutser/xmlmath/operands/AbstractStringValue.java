package cx.prutser.xmlmath.operands;

public abstract class AbstractStringValue extends AbstractValue {

	public abstract String getString();
	
	public final Object getValue() {
		return getString();
	}
}
