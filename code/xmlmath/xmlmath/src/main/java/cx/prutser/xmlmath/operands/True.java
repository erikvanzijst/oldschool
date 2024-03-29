package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.ValueType;

/**
 * True.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	1.0, 12.mar.2006
 */
public final class True extends AbstractBooleanValue {

	@Override
	public Boolean getBoolean() {
		return true;
	}
	
	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operants recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {
		return;
	}
}
