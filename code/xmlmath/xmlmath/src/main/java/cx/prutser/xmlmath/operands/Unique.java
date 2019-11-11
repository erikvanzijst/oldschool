package cx.prutser.xmlmath.operands;

import java.util.ArrayList;
import java.util.List;

import cx.prutser.xmlmath.schemas.UniqueType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * Filters all duplicate items from the list and returns the result. The
 * ordering is the same as in the original list.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	1.0, 11.mar.2006
 */
public class Unique extends AbstractListValue {

	private AbstractListValue list = null;
	
	/**
	 * @return	the same list as the given list, only with all duplicate items
	 * removed. The ordering is the same as in the original list.
	 */
	@Override
	public List<Object> getList() {
		
		List<Object> unique = new ArrayList<Object>();
		
		for (Object item : list.getList()) {
			if(!unique.contains(item)) {
				unique.add(item);
			}
		}
		return unique;
	}
	
	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operants recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		UniqueType xml = (UniqueType)xmlObject;
		list = (AbstractListValue)AbstractValue.parse( xml.getAbstractList() );
	}
}
