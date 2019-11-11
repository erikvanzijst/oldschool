package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.StrlenType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * Computes the length of a string.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	1.0, 12.mar.2006
 */
public class StringLength extends AbstractLongValue {

	private AbstractStringValue string = null;
	
	/**
	 * @return the length of the given string.
	 */
	@Override
	public Long getLong() {
		return new Long(string.getString().length());
	}

	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operands recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		StrlenType xml = (StrlenType)xmlObject;
		string = (AbstractStringValue)AbstractValue.parse(xml.getAbstractString());
	}
}
