package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.StringType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * Represents a literal string value.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	1.0, 07.apr.2006
 */
public class StringValue extends AbstractStringValue {

	private String string = null;
	
	@Override
	public String getString() {
		return string;
	}
	
	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operants recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		StringType xml = (StringType)xmlObject;
		string = xml.getValue();
	}
}
