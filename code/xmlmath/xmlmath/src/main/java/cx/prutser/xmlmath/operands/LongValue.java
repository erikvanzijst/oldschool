package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.LongType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * 
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	1.0, 06.apr.2006
 */
public class LongValue extends AbstractLongValue {

	private Long integer = null;
	
	@Override
	public Long getLong() {
		return integer;
	}
	
	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operands recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		LongType xml = (LongType)xmlObject;
		integer = xml.getValue();
	}
}
