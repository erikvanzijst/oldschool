package cx.prutser.xmlmath.operands;

/**
 * Base type for double-precision 64-bit IEEE 754 floating point numbers.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	1.0, 07.apr.2006
 */
public abstract class AbstractDoubleValue extends AbstractNumberValue {

	public abstract Double getDouble();
	
	@Override
	public final Number getNumber() {
		return getDouble();
	}
}
