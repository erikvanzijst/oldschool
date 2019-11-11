package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.ListProductType;
import cx.prutser.xmlmath.schemas.NumberType2;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * Computes the product of all numbers in the given list.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	v1.0, 10.mar.2006
 */
public class ListProduct extends AbstractNumberValue {

	/**
	 * Used to specify whether the list's elements must be interpreted as
	 * longs or doubles. Defaults to DOUBLE when omitted.
	 */
	private NumberType2.Enum type = NumberType2.DOUBLE;

	/**
	 * This class expects a list whose elements are all of type
	 * <tt>java.lang.Number</tt>.
	 */
	private AbstractListValue list = null;

	/**
	 * @return	the product of all the list's items multiplied.
	 * @exception EvaluationException when an empty list is provided.
	 * @exception TypeCastException when the list is not homogeneous.
	 */
	@Override
	public Number getNumber() throws EvaluationException, TypeCastException {

		Number product = null;
		boolean first = true;

		try {
			if(type == NumberType2.LONG) {
	
				long intProduct = 0;
				for(Object item : list.getList()) {
					if(first) {
						intProduct = ((Number)item).longValue();
						first = false;
					} else {
						intProduct *= ((Number)item).longValue();
					}
				}
				product = Long.valueOf(intProduct);
			} else if(type == NumberType2.DOUBLE) {
				
				double doubleProduct = 0.0f;
				for(Object item : list.getList()) {
					if(first) {
						doubleProduct = ((Number)item).doubleValue();
						first = false;
					} else {
						doubleProduct *= ((Number)item).doubleValue();
					}
				}
				product = Double.valueOf(doubleProduct);
			} else {
				assert false : "Unsupported datatype.";
			}
		} catch(ClassCastException cce) {
			throw new TypeCastException("Incompatible cast. List items must " +
					"all be of type number.");
		}

		if(first) {
			throw new EvaluationException("Cannot compute the product of " +
					"an empty list.");
		} else {
			return product;
		}
	}
	
	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operands recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		ListProductType xml = (ListProductType)xmlObject;
		type = xml.getDatatype();
		list = (AbstractListValue)AbstractValue.parse( xml.getAbstractList() );
	}
}
