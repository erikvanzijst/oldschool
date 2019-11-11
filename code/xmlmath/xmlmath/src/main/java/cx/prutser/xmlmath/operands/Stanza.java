package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.StanzaType;

/**
 * 
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	v1.0, 11.may.2006
 */
public class Stanza {

	private final String name;
	private AbstractValue value = null;

	public Stanza(String name) {
		this.name = name;
	}

	public final String getName() {
		return name;
	}

	public Object getValue() {
		return value.getValue();
	}
	
	protected void build(StanzaType xmlStanza) throws ParseException {
		value = AbstractValue.parse(xmlStanza.getValue());
	}

	/**
	 * 
	 * @param xmlStanza
	 * @return
	 * @throws ParseException if the xml stanza element could not be parsed,
	 * 	due to an incorrect or missing java class mapping.
	 */
	public static Stanza parse(StanzaType xmlStanza)
		throws ParseException {
	
		Stanza stanza = new Stanza(xmlStanza.getName());
	
		stanza.build(xmlStanza);
		return stanza;
	}
}
