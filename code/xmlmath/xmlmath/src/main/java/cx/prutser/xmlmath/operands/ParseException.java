package cx.prutser.xmlmath.operands;

/**
 * Thrown when a sudoku xml file cannot be parsed, or when the corresponding
 * evalution trees cannot be constructed from the specified classes.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	1.0, 10.mar.2006
 */
public class ParseException extends Exception {

	private static final long serialVersionUID = 7657685644177132216L;

	public ParseException() {
	}

	public ParseException(String message) {
		super(message);
	}

	public ParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParseException(Throwable cause) {
		super(cause);
	}
}
