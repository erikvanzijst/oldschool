package cx.prutser.xmlmath.operands;

/**
 * 
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	1.0, 12.mar.2006
 */
public class DivideByZeroException extends EvaluationException {

	private static final long serialVersionUID = -1433253662318173173L;

	public DivideByZeroException() {
	}

	public DivideByZeroException(String message) {
		super(message);
	}
}
