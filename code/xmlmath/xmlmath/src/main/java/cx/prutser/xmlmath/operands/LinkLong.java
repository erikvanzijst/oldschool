package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.LinkLongType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * Convenience link to declarations of type <tt>Long</tt>.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	v1.1, 18.apr.2006
 */
public class LinkLong extends AbstractLongValue {

	private Declaration longDeclaration = null;
	
	@Override
	public Long getLong() throws TypeCastException {
		try {
			return (Long)longDeclaration.getValue();
		} catch(ClassCastException cce) {
			throw new TypeCastException("Incompatible cast from " +
					cce.getMessage() + " to Long.");
		}
	}
	
	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operands recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		LinkLongType xml = (LinkLongType)xmlObject;
		
		longDeclaration = Scope.getScope().getDeclaration(xml.getName());
		if(longDeclaration == null) {
			throw new ParseException("Reference attempted to undeclared " +
					"variable " + xml.getName());
		}
	}
}
