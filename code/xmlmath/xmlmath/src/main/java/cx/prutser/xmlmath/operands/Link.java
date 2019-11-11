package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.LinkType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	v1.1, 18.apr.2006
 */
public final class Link extends AbstractValue {

	private Declaration declaration = null;
	
	/**
	 * Evaluates and returns the value of the expression of the equally named
	 * declare-tag that was declared higher up in the expression tree.
	 */
	@Override
	public Object getValue() throws EvaluationException {
		return declaration.getValue();
	}

	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operants recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {
		
		LinkType xml = (LinkType)xmlObject;
		
		declaration = Scope.getScope().getDeclaration(xml.getName());
		if(declaration == null) {
			throw new ParseException("Reference attempted to undeclared " +
					"variable " + xml.getName());
		}
	}
}
