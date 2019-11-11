package cx.prutser.xmlmath.operands;

/**
 * Abstract base class for all 64-bit long values.
 *
 * @author	Erik van Zijst - erik@marketxs.com
 * @version	1.0, 07.apr.2006
 */
public abstract class AbstractLongValue extends AbstractNumberValue {

	public abstract Long getLong();

	@Override
	public final Number getNumber() {
		return getLong();
	}
}
