package cx.prutser.xmlmath.operands;

import java.util.List;

import cx.prutser.xmlmath.schemas.InlineListType;
import cx.prutser.xmlmath.schemas.StanzaType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * Convenience tag to inline stanza's that return type <tt>List</tt>.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	v1.0, 12.may.2006
 */
public class InlineList extends AbstractListValue {

	private Stanza listStanza = null;

	@Override
	public List<Object> getList() throws TypeCastException {
		
		try {
			return (List<Object>)listStanza.getValue();
		} catch(ClassCastException cce) {
			throw new TypeCastException("Incompatible cast from " +
					cce.getMessage() + " to List.");
		}
	}

	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		InlineListType xml = (InlineListType)xmlObject;
		
		StanzaType stanzaType = Scope.getScope().getStanzaType(xml.getName());
		if(stanzaType == null) {
			throw new ParseException("Reference attempted to undeclared " +
					"stanza " + xml.getName());
		}
		listStanza = Stanza.parse(stanzaType);
	}
}
