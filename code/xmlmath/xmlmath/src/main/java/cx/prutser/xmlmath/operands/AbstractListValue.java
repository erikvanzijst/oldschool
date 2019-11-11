package cx.prutser.xmlmath.operands;

import java.util.List;

public abstract class AbstractListValue extends AbstractValue {

	public abstract List<Object> getList();
	
	public final Object getValue() {
		return getList();
	}
}
