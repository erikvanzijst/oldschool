package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.Log10Type;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * Computes the common logarithm (base 10) of a double  value. If the argument
 * is positive infinity, then the result is positive infinity. If the argument
 * is positive zero or negative zero, then the result is negative infinity. If
 * the argument is equal to 10^n for integer n, then the result is n.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	v1.0, 14.apr.2006
 */
public class Log10 extends AbstractDoubleValue {

	private AbstractDoubleValue operand = null;

	/**
	 * @return common logarithm (base 10) of the operand.
	 */
	@Override
	public Double getDouble() {
		return Math.log10(operand.getDouble());
	}

	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operands recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		Log10Type xml = (Log10Type)xmlObject;
		operand = (AbstractDoubleValue)AbstractValue.parse(xml.getAbstractDouble());
	}
}
