package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.LinkDoubleType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * Convenience link to declarations of type <tt>Double</tt>.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	v1.1, 18.apr.2006
 */
public class LinkDouble extends AbstractDoubleValue {

	private Declaration doubleDeclaration = null;
	
	@Override
	public Double getDouble() throws TypeCastException {
		
		try {
			return (Double)doubleDeclaration.getValue();
		} catch(ClassCastException cce) {
			throw new TypeCastException("Incompatible cast from " +
					cce.getMessage() + " to Double.");
		}
	}
	
	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operands recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		LinkDoubleType xml = (LinkDoubleType)xmlObject;
		
		doubleDeclaration = Scope.getScope().getDeclaration(xml.getName());
		if(doubleDeclaration == null) {
			throw new ParseException("Reference attempted to undeclared " +
					"variable " + xml.getName());
		}
	}
}
