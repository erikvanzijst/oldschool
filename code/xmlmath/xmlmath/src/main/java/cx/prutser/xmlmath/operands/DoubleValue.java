package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.DoubleType;
import cx.prutser.xmlmath.schemas.ValueType;

public class DoubleValue extends AbstractDoubleValue {

	private Double f = null;
	
	@Override
	public Double getDouble() {
		return f;
	}
	
	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operants recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		DoubleType xml = (DoubleType)xmlObject;
		f = xml.getValue();
	}
}
