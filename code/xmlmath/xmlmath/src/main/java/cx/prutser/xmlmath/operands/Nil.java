package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.ValueType;

/**
 * Returns <tt>null</tt>.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	1.0, 15.mar.2006
 */
public final class Nil extends AbstractValue {

	/**
	 * @return <tt>null</tt>.
	 */
	@Override
	public Object getValue() throws EvaluationException {
		return null;
	}

	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operants recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {
		return;
	}
}
