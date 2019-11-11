package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.ModType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * Computes <tt>integer % denominator</tt> where the denominator is the second
 * operand.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	1.0, 12.mar.2006
 */
public class Mod extends AbstractLongValue {

	private AbstractLongValue integer = null;
	private AbstractLongValue denominator = null;

	/**
	 * Returns <tt>integer % denominator</tt>.
	 */
	@Override
	public Long getLong() {
		return integer.getLong() % denominator.getLong();
	}
	
	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operands recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		ModType xml = (ModType)xmlObject;
		integer = (AbstractLongValue)AbstractValue.parse( xml.getAbstractLongArray(0) );
		denominator = (AbstractLongValue)AbstractValue.parse( xml.getAbstractLongArray(1) );
	}
}
