package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.FactType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * Computes the factorial of a long with a simple, non-recursive algorithm.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	1.0, 12.mar.2006
 */
public class Factorial extends AbstractLongValue {

	private AbstractLongValue integer = null;

	/**
	 * @return !integer.
	 * @exception EvaluationException if a negative long is provided.
	 */
	@Override
	public Long getLong() throws EvaluationException {
		
		long result = integer.getLong();

		if(result > 0) {
			for(long nX = result - 1; nX > 0; result *= nX--);
		}
		else if(result == 0) {
			result = 1;
		} else {
			throw new EvaluationException("Cannot compute the factorial of " +
					"a negative integer.");
		}

		return result;
	}

	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operants recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		FactType xml = (FactType)xmlObject;
		integer = (AbstractLongValue)AbstractValue.parse( xml.getAbstractLong() );
	}
}
