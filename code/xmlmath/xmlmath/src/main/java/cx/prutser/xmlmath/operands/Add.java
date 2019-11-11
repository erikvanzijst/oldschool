package cx.prutser.xmlmath.operands;

import java.util.ArrayList;
import java.util.List;

import cx.prutser.xmlmath.schemas.AddType;
import cx.prutser.xmlmath.schemas.NumberType;
import cx.prutser.xmlmath.schemas.NumberType2;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * Computes the sum of two or more numbers.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	1.0, 11.mar.2006
 */
public class Add extends AbstractNumberValue {

	/**
	 * Used to specify whether the list's elements must be interpreted as
	 * longs or doubles. Defaults to DOUBLE when omitted.
	 */
	private NumberType2.Enum type = NumberType2.DOUBLE;

	private AbstractNumberValue[] numbers = null;

	@Override
	public Number getNumber() {

		Number sum = null;
		
		if(type == NumberType2.LONG) {
			long result = 0;
			for(int nX = 0; nX < numbers.length; nX++) {
				result += numbers[nX].getNumber().longValue();
			}
			sum = Long.valueOf(result);
		} else if(type == NumberType2.DOUBLE) {
			double result = 0;
			for(int nX = 0; nX < numbers.length; nX++) {
				result += numbers[nX].getNumber().doubleValue();
			}
			sum = Double.valueOf(result);
		}
		return sum;
	}

	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		AddType xml = (AddType)xmlObject;
		type = xml.getDatatype();
		List<AbstractNumberValue> buf = new ArrayList<AbstractNumberValue>();
		for (NumberType string : xml.getNumberList()) {
			buf.add( (AbstractNumberValue)AbstractValue.parse(string) );
		}
		numbers = buf.toArray(new AbstractNumberValue[buf.size()]);
	}
}
