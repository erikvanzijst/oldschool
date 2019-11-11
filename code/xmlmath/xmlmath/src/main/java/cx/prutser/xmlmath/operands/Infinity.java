package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.ValueType;

/**
 * A constant holding the positive infinity of type double.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	1.0, 12.mar.2006
 */
public final class Infinity extends AbstractDoubleValue {

	@Override
	public Double getDouble() {
		return Double.POSITIVE_INFINITY;
	}

	@Override
	protected void build(ValueType xmlObject) throws ParseException {
		return;
	}
}
