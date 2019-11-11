package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.NumberType2;
import cx.prutser.xmlmath.schemas.ProductType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * Computes the product of two numbers.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	1.0, 11.mar.2006
 */
public class Product extends AbstractNumberValue {

	/**
	 * Used to specify whether the list's elements must be interpreted as
	 * longs or doubles. Defaults to DOUBLE when omitted.
	 */
	private NumberType2.Enum type = NumberType2.DOUBLE;

	private AbstractNumberValue operand1 = null;
	private AbstractNumberValue operand2 = null;

	@Override
	public Number getNumber() {

		Number product = null;
		
		if(type == NumberType2.LONG) {
			product = Long.valueOf(operand1.getNumber().longValue() *
				operand2.getNumber().longValue());
		} else if(type == NumberType2.DOUBLE) {
			product = Double.valueOf(operand1.getNumber().doubleValue() *
				operand2.getNumber().doubleValue());
		}
		return product;
	}

	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		ProductType xml = (ProductType)xmlObject;
		type = xml.getDatatype();
		operand1 = (AbstractNumberValue)AbstractValue.parse(xml.getNumberArray(0));
		operand2 = (AbstractNumberValue)AbstractValue.parse(xml.getNumberArray(1));
	}
}
