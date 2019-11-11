package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.InlineType;
import cx.prutser.xmlmath.schemas.StanzaType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * 
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	v1.0, 11.may.2006
 */
public class Inline extends AbstractValue {

	private Stanza stanza = null;
	
	/**
	 * Evaluates and returns the value of the expression of the equally named
	 * stanza-tag that was defined higher up in the expression tree.
	 */
	@Override
	public Object getValue() throws EvaluationException {
		return stanza.getValue();
	}

	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		InlineType xml = (InlineType)xmlObject;
		
		StanzaType stanzaType = Scope.getScope().getStanzaType(xml.getName());
		if(stanzaType == null) {
			throw new ParseException("Reference attempted to undeclared " +
					"stanza " + xml.getName());
		}
		stanza = Stanza.parse(stanzaType);
	}
}
