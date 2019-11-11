package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.InlineStringType;
import cx.prutser.xmlmath.schemas.StanzaType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * Convenience tag to inline stanza's that return type <tt>String</tt>.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	v1.0, 12.may.2006
 */
public class InlineString extends AbstractStringValue {

	private Stanza stringStanza = null;

	@Override
	public String getString() throws TypeCastException {
		
		try {
			return (String)stringStanza.getValue();
		} catch(ClassCastException cce) {
			throw new TypeCastException("Incompatible cast from " +
					cce.getMessage() + " to String.");
		}
	}

	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		InlineStringType xml = (InlineStringType)xmlObject;
		
		StanzaType stanzaType = Scope.getScope().getStanzaType(xml.getName());
		if(stanzaType == null) {
			throw new ParseException("Reference attempted to undeclared " +
					"stanza " + xml.getName());
		}
		stringStanza = Stanza.parse(stanzaType);
	}
}
