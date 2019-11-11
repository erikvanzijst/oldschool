package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.InlineLongType;
import cx.prutser.xmlmath.schemas.StanzaType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * Convenience tag to inline stanza's that return type <tt>Long</tt>.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	v1.0, 12.may.2006
 */
public class InlineLong extends AbstractLongValue {

	private Stanza longStanza = null;

	@Override
	public Long getLong() throws TypeCastException {
		
		try {
			return (Long)longStanza.getValue();
		} catch(ClassCastException cce) {
			throw new TypeCastException("Incompatible cast from " +
					cce.getMessage() + " to Long.");
		}
	}

	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		InlineLongType xml = (InlineLongType)xmlObject;
		
		StanzaType stanzaType = Scope.getScope().getStanzaType(xml.getName());
		if(stanzaType == null) {
			throw new ParseException("Reference attempted to undeclared " +
					"stanza " + xml.getName());
		}
		longStanza = Stanza.parse(stanzaType);
	}
}
