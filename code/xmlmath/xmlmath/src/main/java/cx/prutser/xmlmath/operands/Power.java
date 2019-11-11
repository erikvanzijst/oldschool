package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.PowType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * Returns the value of the first argument raised to the power of the second
 * argument. For special cases, refer to
 * {@link java.lang.Math#pow(double, double)}.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	1.0, 12.mar.2006
 */
public class Power extends AbstractDoubleValue {

	private AbstractDoubleValue base = null;
	private AbstractDoubleValue exponent = null;

	/**
	 * @return the result of argument1 raised to the power of argument2.
	 */
	@Override
	public Double getDouble() {
		
		return Math.pow(base.getDouble(), exponent.getDouble());
	}

	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operants recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		PowType xml = (PowType)xmlObject;
		base = (AbstractDoubleValue)AbstractValue.parse(xml.getAbstractDoubleArray(0));
		exponent = (AbstractDoubleValue)AbstractValue.parse(xml.getAbstractDoubleArray(1));
	}
}
