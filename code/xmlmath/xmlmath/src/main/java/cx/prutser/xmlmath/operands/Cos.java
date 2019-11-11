package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.CosType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * Computes the trigonometric cosine of an angle.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	1.0, 12.mar.2006
 */
public class Cos extends AbstractDoubleValue {

	private AbstractDoubleValue angle = null;

	/**
	 * @return the trigonometric cosine of an angle.
	 */
	@Override
	public Double getDouble() {
		return Math.cos(angle.getDouble());
	}

	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operands recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		CosType xml = (CosType)xmlObject;
		angle = (AbstractDoubleValue)AbstractValue.parse(xml.getAbstractDouble());
	}
}
