package cx.prutser.xmlmath.operands;

public abstract class AbstractNumberValue extends AbstractValue {

	public abstract Number getNumber();

	public final Object getValue() {
		return getNumber();
	}
}
