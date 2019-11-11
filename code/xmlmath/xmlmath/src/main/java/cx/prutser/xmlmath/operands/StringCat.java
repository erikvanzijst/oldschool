package cx.prutser.xmlmath.operands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cx.prutser.xmlmath.schemas.AbstractStringType;
import cx.prutser.xmlmath.schemas.StrcatType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * Concatenates two or more individually declared strings, or all the elements
 * of a homogeneous string list.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	v1.0, 12.mar.2006
 */
public class StringCat extends AbstractStringValue {

	private AbstractStringValue[] strings = null;
	private AbstractListValue stringList = null;

	/**
	 * @return <tt>string1 + string2 + ...</tt>.
	 */
	@Override
	public String getString() {
		
		StringBuffer buf = new StringBuffer();
		
		if(stringList != null) {
			for (Iterator it = stringList.getList().iterator();
				it.hasNext(); buf.append((String)it.next()));
		} else {
			for (int nX = 0; nX < strings.length;
				buf.append(strings[nX++].getString()));
		}
		return buf.toString();
	}

	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operands recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		StrcatType xml = (StrcatType)xmlObject;
		
		if(xml.isSetAbstractList()) {
			stringList = (AbstractListValue)AbstractValue.parse(xml.getAbstractList());
		} else {
			List<AbstractStringValue> buf = new ArrayList<AbstractStringValue>();
			for (AbstractStringType string : xml.getAbstractStringList()) {
				buf.add( (AbstractStringValue)AbstractValue.parse(string) );
			}
			strings = buf.toArray(new AbstractStringValue[buf.size()]);
		}
	}
}
