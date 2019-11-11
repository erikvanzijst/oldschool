package cx.prutser.xmlmath.operands;

import java.util.ArrayList;
import java.util.List;

import cx.prutser.xmlmath.schemas.AbstractBooleanType;
import cx.prutser.xmlmath.schemas.AndType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * Returns <tt>true</tt> if all operands are <tt>true</tt>.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	1.0, 12.mar.2006
 */
public class And extends AbstractBooleanValue {

	private AbstractBooleanValue[] booleans = null;

	/**
	 * @return the result of <tt>operand1 && operand2 && ...</tt>.
	 */
	@Override
	public Boolean getBoolean() {
		
		for (AbstractBooleanValue bool : booleans) {
			if(!bool.getBoolean()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operands recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		AndType xml = (AndType)xmlObject;
		List<AbstractBooleanValue> buf = new ArrayList<AbstractBooleanValue>();
		for (AbstractBooleanType bool : xml.getAbstractBooleanList()) {
			buf.add((AbstractBooleanValue)AbstractValue.parse(bool));
		}
		booleans = buf.toArray(new AbstractBooleanValue[buf.size()]);
	}
}
