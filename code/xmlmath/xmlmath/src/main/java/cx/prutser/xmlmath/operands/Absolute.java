package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.AbsType;
import cx.prutser.xmlmath.schemas.NumberType2;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * Computes absolute value of a number.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	1.0, 12.mar.2006
 */
public class Absolute extends AbstractNumberValue {

	/**
	 * Used to specify whether the list's elements must be interpreted as
	 * longs or doubles. Defaults to DOUBLE when omitted.
	 */
	private NumberType2.Enum type = NumberType2.DOUBLE;
	private AbstractNumberValue number = null;
	
	/**
	 * @return the absolute value of the given operand.
	 */
	@Override
	public Number getNumber() {
		
		Number result = null;
		
		if(type == NumberType2.LONG) {
			result =  Long.valueOf(Math.abs(
					number.getNumber().longValue()));
		} else if(type == NumberType2.DOUBLE) {
			result = Double.valueOf(Math.abs(
					number.getNumber().doubleValue()));
		} else {
			assert false : "Unsupported datatype.";
		}
		return result;
	}

	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operands recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		AbsType xml = (AbsType)xmlObject;
		type = xml.getDatatype();
		number = (AbstractNumberValue)AbstractValue.parse(xml.getNumber());
	}
}
