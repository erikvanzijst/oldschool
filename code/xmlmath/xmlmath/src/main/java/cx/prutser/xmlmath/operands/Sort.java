package cx.prutser.xmlmath.operands;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cx.prutser.xmlmath.schemas.SortOrder;
import cx.prutser.xmlmath.schemas.SortType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * Sorts the specified list into ascending order, according to the natural
 * ordering of its elements. All elements in the list must be of the same
 * type.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	1.0, 12.mar.2006
 */
public class Sort extends AbstractListValue {

	private AbstractListValue list = null;
	private SortOrder.Enum order = SortOrder.ASCENDING;
	
	private final Comparator descender =
		new Comparator() {
			public int compare(Object o1, Object o2) {
					return ((Comparable)o2).compareTo(o1);
				}
		};
	
	/**
	 * @return	the same list as the given list, only with all duplicate items
	 * 	removed. The ordering is the same as in the original list.
	 * @exception when the list is not homogeneous in nature.
	 */
	@Override
	public List<Object> getList() throws EvaluationException {

		// TODO: fix the generics issue properly
		List result = list.getList();
		try {
			Collections.sort(result, order == SortOrder.ASCENDING ?
					null : descender);
			return result;
		} catch(ClassCastException cce) {
			throw new EvaluationException("Cannot sort a heterogeneous list. " +
					"Make sure all elements are of the same type.");
		}
	}

	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		SortType xml = (SortType)xmlObject;
		order = xml.getOrder();
		list = (AbstractListValue)AbstractValue.parse( xml.getAbstractList() );
	}
}
