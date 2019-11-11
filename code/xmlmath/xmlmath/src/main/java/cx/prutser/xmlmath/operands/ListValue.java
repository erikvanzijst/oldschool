package cx.prutser.xmlmath.operands;

import java.util.ArrayList;
import java.util.List;

import cx.prutser.xmlmath.schemas.ListType;
import cx.prutser.xmlmath.schemas.ValueType;

public class ListValue extends AbstractListValue {

	private List<AbstractValue> arguments = new ArrayList<AbstractValue>();
	
	public void addItem(AbstractValue item) {
		arguments.add(item);
	}
	
	/**
	 * @return	a list that contains all entries whose value is not
	 * <tt>null</tt>.
	 */
	@Override
	public List<Object> getList() {
		
		List<Object> list = new ArrayList<Object>();
		
		for (AbstractValue arg : arguments) {
			if(arg.getValue() != null) {
				list.add(arg.getValue());
			}
		}
		return list;
	}
	
	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operands recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		ListType xml = (ListType)xmlObject;
		
		for(ValueType item : xml.getValueList()) {
			arguments.add( AbstractValue.parse(item) );
		}
	}
}
