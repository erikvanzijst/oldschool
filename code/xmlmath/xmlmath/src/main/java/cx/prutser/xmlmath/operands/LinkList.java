package cx.prutser.xmlmath.operands;

import java.util.List;

import cx.prutser.xmlmath.schemas.LinkListType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * Convenience link to declarations of type <tt>List</tt>.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	v1.1, 18.apr.2006
 */
public class LinkList extends AbstractListValue {

	private Declaration listDeclaration = null;

	@Override
	public List<Object> getList() throws TypeCastException {
		
		try {
			return (List<Object>)listDeclaration.getValue();
		} catch(ClassCastException cce) {
			throw new TypeCastException("Incompatible cast from " +
					cce.getMessage() + " to List.");
		}
	}
	
	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operands recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		LinkListType xml = (LinkListType)xmlObject;
		
		listDeclaration = Scope.getScope().getDeclaration(xml.getName());
		if(listDeclaration == null) {
			throw new ParseException("Reference attempted to undeclared " +
					"variable " + xml.getName());
		}
	}
}
