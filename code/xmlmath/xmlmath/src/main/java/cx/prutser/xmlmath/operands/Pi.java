package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.ValueType;

/**
 * The double value that is closer than any other to pi, the ratio of the
 * circumference of a circle to its diameter.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	1.0, 12.mar.2006
 */
public final class Pi extends AbstractDoubleValue {

	@Override
	public Double getDouble() {
		return Math.PI;
	}

	@Override
	protected void build(ValueType xmlObject) throws ParseException {
		return;
	}
}
