package cx.prutser.xmlmath.operands;

import org.apache.xmlbeans.XmlObject;

import cx.prutser.xmlmath.schemas.NumberType2;
import cx.prutser.xmlmath.schemas.SumType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * Evaluates its operand in a loop (to - from) times and computes the sum of
 * all results. This operation represents the capital sigma. The iterator is
 * automatically declared as a long variable which can be referenced from
 * within the sum's expression.
 * 
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	v1.0, Apr 12, 2006
 */
public class Sum extends AbstractNumberValue {

	/**
	 * Used to specify whether the list's elements must be interpreted as
	 * longs or doubles. Defaults to DOUBLE when omitted.
	 */
	private NumberType2.Enum type = NumberType2.DOUBLE;

	private AbstractNumberValue operand = null;
	private IndexDeclaration iterator = null;
	private AbstractLongValue from = null;
	private AbstractLongValue to = null;
	
	@Override
	public Number getNumber() {
		
		Number sum = null;

		int end = to.getLong().intValue();

		switch(type.intValue()) {

			case NumberType2.INT_DOUBLE: {
				double _sum = 0;
				for(iterator.setIndex(from.getLong()); iterator.getIndex() <= end;
					iterator.increment()) {
					_sum += operand.getNumber().doubleValue();
				}
				sum = Double.valueOf(_sum);
				break;
			}
			
			case NumberType2.INT_LONG: {
				long _sum = 0;
				for(iterator.setIndex(from.getLong()); iterator.getIndex() <= end;
					iterator.increment()) {
					_sum += operand.getNumber().longValue();
				}
				sum = Long.valueOf(_sum);
				break;
			}
			
			default:
				assert false : "Unsupported data type: " + type.toString();
		}
		
		return sum;
	}
	
	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operands recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		SumType xml = (SumType)xmlObject;
		type = xml.getDatatype();
		Scope.getScope().setDeclaration(
				iterator = new IndexDeclaration(xml.getIterator()));
		from = (AbstractLongValue)AbstractValue.parse(xml.getAbstractLongArray(0));
		to = (AbstractLongValue)AbstractValue.parse(xml.getAbstractLongArray(1));

		/*
		 * This "manual" xpath query is necessary to select the last child
		 * element of the sum-loop. Using the getValue() method unfortunately
		 * selects the first child element (startIndex) because it is also a
		 * ValueType. Of course the xpath query could be more elegant.
		 */
		XmlObject[] elements = xml.selectPath("*");
		operand = (AbstractNumberValue)AbstractValue.parse(
				(ValueType)elements[elements.length - 1]);
	}
	
	/**
	 * TODO: place these methods as static in the LongDeclaration type.
	 * 
	 * @param name
	 * @return
	 * @throws ParseException
	 */
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
