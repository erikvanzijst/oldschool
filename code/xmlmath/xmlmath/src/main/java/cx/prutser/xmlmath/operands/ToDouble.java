package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.ToDoubleType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * Converts any number to a double value.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	1.0, 09.mar.2006
 */
public class ToDouble extends AbstractDoubleValue {

	private AbstractNumberValue number = null;
	
	@Override
	public Double getDouble() {
		return number.getNumber().doubleValue();
	}
	
	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operands recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		ToDoubleType xml = (ToDoubleType)xmlObject;
		number = (AbstractNumberValue)AbstractValue.parse( xml.getNumber() );
	}
}
