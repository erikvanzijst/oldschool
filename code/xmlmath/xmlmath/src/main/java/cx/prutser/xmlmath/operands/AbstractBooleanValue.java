package cx.prutser.xmlmath.operands;

public abstract class AbstractBooleanValue extends AbstractValue {

	public abstract Boolean getBoolean();
	
	public final Object getValue() {
		return getBoolean();
	}
}
