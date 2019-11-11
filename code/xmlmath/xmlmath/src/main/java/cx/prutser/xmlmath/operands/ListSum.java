package cx.prutser.xmlmath.operands;

import java.util.List;

import cx.prutser.xmlmath.schemas.ListSumType;
import cx.prutser.xmlmath.schemas.NumberType2;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * 
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	v1.0, 19.apr.2006
 */
public class ListSum extends AbstractNumberValue {

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
	 * @return the sum of all the list's items. 
	 * @exception EvaluationException when an empty list is provided.
	 * @exception TypeCastException when the list is not homogeneous.
	 */
	@Override
	public Number getNumber() throws EvaluationException, TypeCastException {
		
		Number sum = null;
		List<Object> values = list.getList();
		
		if(values.size() == 0) {
			throw new EvaluationException("Cannot compute the sum of an " +
					"empty list.");
		} else {

			try {
				if(type == NumberType2.LONG) {
				
					long longResult = 0;
					for (Object object : values) {
						longResult += ((Number)object)
							.longValue();
					}
					sum = Long.valueOf(longResult);
				} else if(type == NumberType2.DOUBLE) {
						
					double doubleResult = 0.0f;
					for (Object object : values) {
						doubleResult += ((Number)object)
							.doubleValue();
					}
					sum = Double.valueOf(doubleResult);
				} else {
					assert false : "Unsupported datatype.";
				}
			} catch(ClassCastException cce) {
				throw new TypeCastException("Incompatible cast. List items " +
						"must all be of type number.");
			}
			
			return sum;
		}
	}
	
	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operants recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		ListSumType xml = (ListSumType)xmlObject;
		type = xml.getDatatype();
		list = (AbstractListValue)AbstractValue.parse( xml.getAbstractList() );
	}
}
