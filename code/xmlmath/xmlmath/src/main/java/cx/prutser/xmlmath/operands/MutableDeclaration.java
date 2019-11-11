package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.DeclareType;

/**
 * 
 *
 * @author	Erik van Zijst - erik@marketxs.com
 * @version	v1.1, 18.apr.2006
 */
public class MutableDeclaration extends Declaration {

	private Object value = null;
	
	public MutableDeclaration(String name) {
		super(name);
	}
	
	@Override
	public Object getValue() {
		return value;
	}
	
	public void setValue(Object o) {
		this.value = o;
	}

	/**
	 * @exception UnsupportedOperationException because this declaration is
	 * 	for internally.
	 */
	@Override
	protected void build(DeclareType xmlDeclare) throws ParseException {
		throw new UnsupportedOperationException();
	}
}
