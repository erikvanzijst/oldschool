package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.ValueType;
import cx.prutser.xmlmath.schemas.XorType;

/**
 * Performs an exclusive-or on two boolean operands.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	1.0, 10.mar.2006
 */
public class Xor extends AbstractBooleanValue {

	private AbstractBooleanValue arg1 = null;
	private AbstractBooleanValue arg2 = null;

	@Override
	public Boolean getBoolean() {
		return arg1.getBoolean() ^ arg2.getBoolean();
	}
	
	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operands recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		XorType xml = (XorType)xmlObject;
		arg1 = (AbstractBooleanValue)AbstractValue.parse( xml.getAbstractBooleanArray(0) );
		arg2 = (AbstractBooleanValue)AbstractValue.parse( xml.getAbstractBooleanArray(1) );
	}
}
