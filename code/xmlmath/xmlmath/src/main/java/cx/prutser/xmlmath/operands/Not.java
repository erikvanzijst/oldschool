package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.NotType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * Negates its boolean operand.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	1.0, 06.apr.2006
 */
public final class Not extends AbstractBooleanValue {

	private AbstractBooleanValue bool = null;
	
	@Override
	public Boolean getBoolean() {
		return !bool.getBoolean();
	}
	
	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operands recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		NotType xml = (NotType)xmlObject;
		bool = (AbstractBooleanValue)AbstractValue.parse( xml.getAbstractBoolean() );
	}
}
