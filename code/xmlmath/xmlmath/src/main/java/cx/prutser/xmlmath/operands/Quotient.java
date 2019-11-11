package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.NumberType2;
import cx.prutser.xmlmath.schemas.QuotientType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * Computes the quotient of two numbers.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	1.0, 11.mar.2006
 */
public class Quotient extends AbstractNumberValue {

	/**
	 * Used to specify whether the list's elements must be interpreted as
	 * longs or doubles. Defaults to DOUBLE when omitted.
	 */
	private NumberType2.Enum type = NumberType2.DOUBLE;

	private AbstractNumberValue operand1 = null;
	private AbstractNumberValue operand2 = null;

	/**
	 * @return the result of <tt>operand1 / operand2</tt>.
	 * @exception DivideByZeroException when an integer division by zero is
	 * 	attempted.
	 */
	@Override
	public Number getNumber() throws EvaluationException, DivideByZeroException {

		Number sum = null;
		
		try {
			if(type == NumberType2.LONG) {
				sum = Long.valueOf(operand1.getNumber().longValue() /
					operand2.getNumber().longValue());
			} else if(type == NumberType2.DOUBLE) {
				sum = Double.valueOf(operand1.getNumber().doubleValue() /
					operand2.getNumber().doubleValue());
			} else {
				assert false : "Unsupported datatype.";
			}
		} catch(ArithmeticException ae) {
			throw new DivideByZeroException();
		}
		return sum;
	}

	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operands recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		QuotientType xml = (QuotientType)xmlObject;
		type = xml.getDatatype();
		operand1 = (AbstractNumberValue)AbstractValue.parse(xml.getNumberArray(0));
		operand2 = (AbstractNumberValue)AbstractValue.parse(xml.getNumberArray(1));
	}
}
