package cx.prutser.xmlmath.operands;

/**
 * Thrown by {@link AbstractValue#getValue()} when an error occurs during
 * evaluation. For example when a division by zero is attempted.
 *
 * @see		cx.prutser.xmlmath.constraints.DivideByZeroException
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	1.0, 12.mar.2006
 */
public class EvaluationException extends RuntimeException {

	private static final long serialVersionUID = 3213627425360008188L;

	public EvaluationException() {
	}

	public EvaluationException(String message) {
		super(message);
	}

	public EvaluationException(String message, Throwable cause) {
		super(message, cause);
	}

	public EvaluationException(Throwable cause) {
		super(cause);
	}
}
