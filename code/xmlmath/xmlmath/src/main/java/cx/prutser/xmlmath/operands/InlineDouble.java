package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.InlineDoubleType;
import cx.prutser.xmlmath.schemas.StanzaType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * Convenience tag to inline stanza's that return type <tt>Double</tt>.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	v1.0, 12.may.2006
 */
public class InlineDouble extends AbstractDoubleValue {

	private Stanza doubleStanza = null;

	@Override
	public Double getDouble() throws TypeCastException {
		
		try {
			return (Double)doubleStanza.getValue();
		} catch(ClassCastException cce) {
			throw new TypeCastException("Incompatible cast from " +
					cce.getMessage() + " to Double.");
		}
	}

	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		InlineDoubleType xml = (InlineDoubleType)xmlObject;
		
		StanzaType stanzaType = Scope.getScope().getStanzaType(xml.getName());
		if(stanzaType == null) {
			throw new ParseException("Reference attempted to undeclared " +
					"stanza " + xml.getName());
		}
		doubleStanza = Stanza.parse(stanzaType);
	}
}
