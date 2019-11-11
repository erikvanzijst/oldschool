package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.ValueType;

/**
 * The double value that is closer than any other to e, the base of the
 * natural logarithms.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	v1.0, 14.apr.2006
 */
public final class E extends AbstractDoubleValue {

	@Override
	public Double getDouble() {
		return Math.E;
	}

	@Override
	protected void build(ValueType xmlObject) throws ParseException {
		return;
	}
}
