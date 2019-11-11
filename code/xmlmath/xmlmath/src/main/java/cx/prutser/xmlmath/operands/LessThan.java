package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.LessThanType;
import cx.prutser.xmlmath.schemas.ValueType;

public class LessThan extends AbstractBooleanValue {

	/**
	 * Whether the check should be <i>less than or equals to</i>, rather than
	 * just <i>less than</i>.
	 */
	private boolean inclusive = false;
	
	private AbstractNumberValue arg1 = null;
	private AbstractNumberValue arg2 = null;
	
	/**
	 * @return <tt>true</tt> if <tt>arg1</tt> is smaller than <tt>arg2</tt>.
	 * If inclusive is set, also returns <tt>true</tt> when <tt>arg1</tt> is
	 * smaller than, or equal to <tt>arg2</tt>.
	 */
	@Override
	public Boolean getBoolean() {
		
		Comparable c1 = (Comparable)arg1.getNumber();
		Comparable c2 = (Comparable)arg2.getNumber();

		if(inclusive) {
			return c1.compareTo(c2) <= 0;
		} else {
			return c1.compareTo(c2) < 0;
		}
	}
	
	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operants recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		LessThanType xml = (LessThanType)xmlObject;
		inclusive = xml.getInclusive();
		arg1 = (AbstractNumberValue)AbstractValue.parse( xml.getNumberArray(0) );
		arg2 = (AbstractNumberValue)AbstractValue.parse( xml.getNumberArray(1) );
	}
}
