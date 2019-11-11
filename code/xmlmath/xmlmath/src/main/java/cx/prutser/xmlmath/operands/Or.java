package cx.prutser.xmlmath.operands;

import java.util.ArrayList;
import java.util.List;

import cx.prutser.xmlmath.schemas.AbstractBooleanType;
import cx.prutser.xmlmath.schemas.OrType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * 
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	1.0, 06.apr.2006
 */
public class Or extends AbstractBooleanValue {

	private AbstractBooleanValue[] booleans = null;

	@Override
	public Boolean getBoolean() {
		
		for (AbstractBooleanValue bool : booleans) {
			if(bool.getBoolean()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operants recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		OrType xml = (OrType)xmlObject;
		List<AbstractBooleanValue> buf = new ArrayList<AbstractBooleanValue>();
		for (AbstractBooleanType bool : xml.getAbstractBooleanList()) {
			buf.add((AbstractBooleanValue)AbstractValue.parse(bool));
		}
		booleans = buf.toArray(new AbstractBooleanValue[buf.size()]);
	}
}
