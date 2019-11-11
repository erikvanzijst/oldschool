package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.ToLongType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * Used to convert (truncate) any number to a long.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	1.0, 09.mar.2006
 */
public class ToLong extends AbstractLongValue {

	private AbstractNumberValue number = null;
	
	@Override
	public Long getLong() {
		return number.getNumber().longValue();
	}
	
	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operands recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		ToLongType xml = (ToLongType)xmlObject;
		number = (AbstractNumberValue)AbstractValue.parse( xml.getNumber() );
	}
}
