package cx.prutser.xmlmath.operands;

import java.util.ArrayList;
import java.util.List;

import org.apache.xmlbeans.XmlObject;

import cx.prutser.xmlmath.schemas.ForType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * 
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	v1.0, Apr 12, 2006
 */
public class For extends AbstractListValue {

	private AbstractValue operand = null;
	private IndexDeclaration iterator = null;
	private AbstractLongValue from = null;
	private AbstractLongValue to = null;
	
	@Override
	public List<Object> getList() {
		
		List<Object> list = new ArrayList<Object>();

		int end = to.getLong().intValue();
		
		for(iterator.setIndex(from.getLong()); iterator.getIndex() < end;
			iterator.increment()) {
			
			list.add(operand.getValue());
		}
		return list;
	}
	
	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operands recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		ForType xml = (ForType)xmlObject;
		Scope.getScope().setDeclaration(
				iterator = new IndexDeclaration(xml.getIterator()));
		from = (AbstractLongValue)AbstractValue.parse(xml.getAbstractLongArray(0));
		to = (AbstractLongValue)AbstractValue.parse(xml.getAbstractLongArray(1));

		/*
		 * This "manual" xpath query is necessary to select the last child
		 * element of the for loop. Using the getValue() method unfortunately
		 * selects the first child element (startIndex) because it is also a
		 * ValueType. Of course the xpath query could be more elegant.
		 */
		XmlObject[] elements = xml.selectPath("*");
		operand = AbstractValue.parse((ValueType)elements[elements.length - 1]);
	}
	
//	private LongDeclaration getRequiredParam(String name) throws ParseException {
//		
//		LongDeclaration declaration = getParam(name);
//		if(declaration == null) {
//			throw new ParseException("Required parameter \"" + name +
//					"\" not found.");
//		} else {
//			return declaration;
//		}
//	}

	/**
	 * Returns a reference to the declaration with the specified name. If the
	 * parameter's value is numeric, it is interpreted as a literal and this
	 * method will return an instance of a declaration that is not linked to a
	 * declared variable. However, when the parameter's value is not numeric,
	 * it is interpreted as the name of declared variable and the return value
	 * will be a reference to the declared variable.
	 * 
	 * @param name
	 * @return
	 * @throws ParseException
	 */
//	private LongDeclaration getParam(String name) throws ParseException {
//
//		LongDeclaration declaration = null;
//		
//		try {
//			declaration = new LongDeclaration(null);
//			declaration.setLong(Long.parseLong(name, 10));
//		} catch(NumberFormatException e) {
//			
//			try {
//				declaration = (LongDeclaration)Scope.getScope()
//					.getDeclaration(name);
//			} catch(ClassCastException cce) {
//				throw new ParseException("Declaration type mismatch for " +
//						"variable " + name);
//			}
//		}
//		
//		return declaration;
//	}
}
