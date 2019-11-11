package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.RoundType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * Returns the closest long to the argument. The result is rounded to an
 * integer by adding 1/2, taking the floor of the result, and casting the
 * result to type long.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	v1.0, 12.mar.2006
 */
public class Round extends AbstractLongValue {

	private AbstractNumberValue operand = null;

	/**
	 * @return the value of the argument rounded to the nearest long value.
	 */
	@Override
	public Long getLong() {
		
		return Math.round(operand.getNumber().doubleValue());
	}

	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operands recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		RoundType xml = (RoundType)xmlObject;
		operand = (AbstractNumberValue)AbstractValue.parse(xml.getNumber());
	}
}
