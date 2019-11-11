package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.IsNilType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * Tests if the argument is nil (null).
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	1.0, 15.mar.2006
 */
public class IsNil extends AbstractBooleanValue {

	private AbstractValue operand = null;

	/**
	 * @return <tt>true</tt> when the operand is <tt>null</tt>, <tt>false</tt>
	 * 	otherwise.
	 */
	@Override
	public Boolean getBoolean() {
		return operand.getValue() == null;
	}

	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operants recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		IsNilType xml = (IsNilType)xmlObject;
		operand = AbstractValue.parse( xml.getValue() );
	}
}
