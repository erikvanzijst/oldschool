package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.LogType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * Computes the natural logarithm (base e) of a double  value. If the argument
 * is positive infinity, then the result is positive infinity. If the argument
 * is positive zero or negative zero, then the result is negative infinity.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	v1.0, 14.apr.2006
 */
public class Log extends AbstractDoubleValue {

	private AbstractDoubleValue operand = null;

	/**
	 * @return natural logarithm (base e) of the operand.
	 */
	@Override
	public Double getDouble() {
		return Math.log(operand.getDouble());
	}

	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operands recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		LogType xml = (LogType)xmlObject;
		operand = (AbstractDoubleValue)AbstractValue.parse(xml.getAbstractDouble());
	}
}
