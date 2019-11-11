package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.CountType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * Counts the size of a list.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	1.0, 12.mar.2006
 */
public class Count extends AbstractLongValue {

	private AbstractListValue list = null;
	
	@Override
	public Long getLong() throws EvaluationException {
		return new Long(list.getList().size());
	}
	
	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operants recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		CountType xml = (CountType)xmlObject;
		list = (AbstractListValue)AbstractValue.parse( xml.getAbstractList() );
	}
}
