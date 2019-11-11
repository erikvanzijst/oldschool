package cx.prutser.xmlmath.operands;

/**
 * 
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	v1.1, 19.apr.2006
 */
public class TypeCastException extends EvaluationException {

	public TypeCastException() {
	}

	public TypeCastException(String message, Throwable cause) {
		super(message, cause);
	}

	public TypeCastException(String message) {
		super(message);
	}

	public TypeCastException(Throwable cause) {
		super(cause);
	}
}
