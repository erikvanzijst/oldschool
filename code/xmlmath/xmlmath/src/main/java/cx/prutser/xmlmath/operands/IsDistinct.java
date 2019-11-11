package cx.prutser.xmlmath.operands;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cx.prutser.xmlmath.schemas.IsDistinctType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * Returns false if one or more list items are duplicates. True otherwise.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	1.0, 11.mar.2006
 */
public class IsDistinct extends AbstractBooleanValue {

	private AbstractListValue list = null;

	@Override
	public Boolean getBoolean() {
		
		List<Object> org = list.getList();
		Set<Object> unique = new HashSet<Object>(org);
		
		return org.size() == unique.size();
	}

	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		IsDistinctType xml = (IsDistinctType)xmlObject;
		list = (AbstractListValue)AbstractValue.parse(xml.getAbstractList());
	}
}
