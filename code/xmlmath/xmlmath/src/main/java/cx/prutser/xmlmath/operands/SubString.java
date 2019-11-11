package cx.prutser.xmlmath.operands;

import cx.prutser.xmlmath.schemas.SubstrType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * Returns a new string that is a substring of this string. The substring
 * begins at the specified beginIndex and extends to the character at index
 * endIndex - 1. Thus the length of the substring is endIndex-beginIndex.
 * <P>
 * When endIndex is omitted, it defaults to the end of the string.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	1.0, 12.mar.2006
 */
public class SubString extends AbstractStringValue {

	private AbstractStringValue string = null;
	private AbstractLongValue beginIndex = null;
	private AbstractLongValue endIndex = null;

	/**
	 * @return a new string that is a substring of this string. The substring
	 * 	begins at the specified beginIndex and extends to the character at
	 * 	index endIndex - 1. Thus the length of the substring is
	 * 	endIndex-beginIndex.
	 * @exception EvaluationException when either of the indices lay beyond
	 * 	the string's boundaries.
	 */
	@Override
	public String getString() throws EvaluationException {

		try {
			if(endIndex != null) {
				return string.getString().substring(beginIndex.getLong().intValue(),
						endIndex.getLong().intValue());
			} else {
				return string.getString().substring(beginIndex.getLong().intValue());
			}
		} catch(IndexOutOfBoundsException e) {
			throw new EvaluationException(e.getMessage());
		}
	}

	/**
	 * Configures this instance according to the given XMLBeans object. This
	 * also includes parsing the child nodes or operands recursively.
	 */
	@Override
	protected void build(ValueType xmlObject) throws ParseException {

		SubstrType xml = (SubstrType)xmlObject;
		beginIndex = (AbstractLongValue)AbstractValue.parse(xml.getAbstractLongArray(0));
		if(xml.getAbstractLongList().size() == 2) {
			endIndex = (AbstractLongValue)AbstractValue.parse(xml.getAbstractLongArray(1));
		}
		string = (AbstractStringValue)AbstractValue.parse(xml.getAbstractString());
	}
}
