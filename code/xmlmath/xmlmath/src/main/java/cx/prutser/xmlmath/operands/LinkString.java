package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.LinkStringType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * Convenience link to declarations of type <tt>String</tt>.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	v1.1, 18.apr.2006
 */
public class LinkString extends AbstractStringValue {

	private Declaration stringDeclaration = null;
	
	@Override
	public String getString() throws TypeCastException {
		
		try {
			return (String)stringDeclaration.getValue();
		} catch(ClassCastException cce) {
			throw new TypeCastException("Incompatible cast from " +
					cce.getMessage() + " to String.");
		}
	}
	
	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operands recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		LinkStringType xml = (LinkStringType)xmlObject;
		
		stringDeclaration = Scope.getScope().getDeclaration(xml.getName());
		if(stringDeclaration == null) {
			throw new ParseException("Reference attempted to undeclared " +
					"variable " + xml.getName());
		}
	}
}
