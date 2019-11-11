package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.DeclareType;

/**
 * Declarations are used to give a particular sub-expression a name so that it
 * can be referenced from elements deeper in the expression tree. The
 * expression inside a declaration is not evaluated at runtime, unless it is
 * explicitly referenced by the link-tag. It is evaluated each time it is
 * linked.
 *
 * @author	Erik van Zijst - erik@marketxs.com
 * @version	v1.0, 11.apr.2006
 */
public class Declaration {

	private final String name;
	private AbstractValue value = null;
	
	public Declaration(String name) {
		this.name = name;
	}
	
	public final String getName() {
		return name;
	}

	public Object getValue() {
		return value.getValue();
	}
	
	protected void build(DeclareType xmlDeclare) throws ParseException {
		
		value = AbstractValue.parse(xmlDeclare.getValue());
	}

	/**
	 * @param xmlDeclare the XMLBeans stub for this declaration element.
	 * @return
	 * @throws ParseException if the xml declaration element could not be
	 * 	parsed, due to an incorrect or missing java class mapping.
	 */
	public static Declaration parse(DeclareType xmlDeclare)
		throws ParseException {
		
		Declaration declaration = new Declaration(
				xmlDeclare.getName());

		declaration.build(xmlDeclare);
		return declaration;
	}
}
