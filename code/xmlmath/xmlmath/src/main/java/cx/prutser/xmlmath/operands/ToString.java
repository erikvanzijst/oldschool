package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.ToStringType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * Returns a string representation of its operand. When the operand is a list,
 * the string representation consists of a list of the operands elements in
 * their original order, enclosed in square brackets ("[]"). Adjacent elements
 * are separated by the characters ", " (comma and space). The list elements
 * are converted to strings with this operation.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	v1.0, 14.apr.2006
 */
public final class ToString extends AbstractStringValue {

	private AbstractValue operand = null;
	
	/**
	 * @return <tt>null</tt>.
	 */
	@Override
	public String getString() throws EvaluationException {
		return String.valueOf(operand.getValue());
	}

	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operands recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		ToStringType xml = (ToStringType)xmlObject;
		operand = AbstractValue.parse(xml.getValue());
	}
}
