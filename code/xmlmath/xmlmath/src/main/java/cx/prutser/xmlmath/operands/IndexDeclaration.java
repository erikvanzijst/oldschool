package cx.prutser.xmlmath.operands;

/**
 * 
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	v1.1, 18.apr.2006
 */
public class IndexDeclaration extends MutableDeclaration {

	public IndexDeclaration(String name) {
		super(name);
	}
	
	public void increment() {
		setIndex(getIndex() + 1);
	}
	
	public Long getIndex() {
		return (Long)super.getValue();
	}
	
	/**
	 * Returns the current value of the index.
	 * 
	 * @return an instance of type <tt>Long</tt>.
	 */
	@Override
	public Object getValue() {
		return getIndex();
	}

	/**
	 * Sets the value of the long index to the given value. The specified
	 * object must be of type <tt>Long</tt>.
	 */
	@Override
	public void setValue(Object o) {
		setIndex((Long)o);
	}
	
	public void setIndex(Long index) {
		super.setValue(index);
	}
}
