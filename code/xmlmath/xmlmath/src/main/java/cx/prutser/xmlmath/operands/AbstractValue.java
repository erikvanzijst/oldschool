package cx.prutser.xmlmath.operands;

import java.lang.reflect.Method;

import cx.prutser.xmlmath.schemas.DeclareType;
import cx.prutser.xmlmath.schemas.StanzaType;
import cx.prutser.xmlmath.schemas.ValueType;

/**
 * 
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	v1.0, 12.mar.2006
 */
public abstract class AbstractValue {

	public abstract Object getValue() throws EvaluationException;

	protected abstract void build(ValueType xmlObject) throws ParseException;
	
	public static AbstractValue parse(ValueType xmlObject) throws ParseException {
		
		AbstractValue value = null;
		try {
			Method method = xmlObject.getClass()
				.getDeclaredMethod("getClassname", new Class[]{});
			String classname = (String)method.invoke(xmlObject, new Object[]{});

			Class clazz = Class.forName(classname);
			value = AbstractValue.class.cast(clazz.newInstance());
			
		} catch(Exception e) {
			e.printStackTrace();
			throw new ParseException("Unable to instantiate java class for " +
					"constraint element: " + xmlObject.toString() +". Does " +
					"the type definition in the schema come with the mandatory " +
					"fixed attribute classname?", e);
		}
		
		Scope scope = Scope.createScope();
		Scope.push(scope);
		try {
			for(DeclareType var : xmlObject.getDeclareList()) {
				Declaration dec = Declaration.parse(var);
				scope.setDeclaration(dec);
			}
			for(StanzaType stanza : xmlObject.getStanzaList()) {
				scope.setStanzaType(stanza);
			}
			value.build(xmlObject);
		} finally {
			Scope.pop();
		}
		return value;
	}
}
