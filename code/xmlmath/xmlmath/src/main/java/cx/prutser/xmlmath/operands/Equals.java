package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.EqualsType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * Compares two operands of any type. Also returns true when both operands are
 * nil, regardless of their type.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	1.0, 11.mar.2006
 */
public class Equals extends AbstractBooleanValue {

	private AbstractValue arg1 = null;
	private AbstractValue arg2 = null;
	
	@Override
	public Boolean getBoolean() {
		
		if(arg1.getValue() == null) {
			if(arg2.getValue() == null) {
				return true;
			} else {
				return false;
			}
		} else {
			return arg1.getValue().equals(arg2.getValue());
		}
	}
	
	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operants recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		EqualsType xml = (EqualsType)xmlObject;
		arg1 = AbstractValue.parse( xml.getValueArray(0) );
		arg2 = AbstractValue.parse( xml.getValueArray(1) );
	}
}
