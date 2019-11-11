package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.SqrtType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * Returns the correctly rounded positive square root of a double value. For
 * special cases, refer to {@link java.lang.Math#sqrt(double)}.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	1.0, 12.mar.2006
 */
public class Sqrt extends AbstractDoubleValue {

	private AbstractDoubleValue operand = null;

	/**
	 * @return the correctly rounded positive square root of a double value.
	 */
	@Override
	public Double getDouble() {
		
		return Math.sqrt(operand.getDouble());
	}

	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operands recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		SqrtType xml = (SqrtType)xmlObject;
		operand = (AbstractDoubleValue)AbstractValue.parse(xml.getAbstractDouble());
	}
}
