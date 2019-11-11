package cx.prutser.xmlmath;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;

import cx.prutser.xmlmath.operands.AbstractValue;
import cx.prutser.xmlmath.operands.ParseException;
import cx.prutser.xmlmath.schemas.ExpressionDocument;

/**
 * 
 *
 * @author	Erik van Zijst - erik@marketxs.com
 * @version	1.0, 08.apr.2006
 */
public class ExpressionFactory {

	public static AbstractValue parseExpression(String xml)
		throws ParseException {
		
		return parseExpression(new StringReader(xml));
	}
	
	public static AbstractValue parseExpression(InputStream in)
		throws ParseException {
	
		return parseExpression(new InputStreamReader(in));
	}

	public static AbstractValue parseExpression(Reader reader)
		throws ParseException {
		
		XmlOptions xmlOptions = new XmlOptions();
		Collection errors = new ArrayList();
		xmlOptions.setLoadLineNumbers();
		xmlOptions.setErrorListener(errors);
		
		try {
			ExpressionDocument doc = ExpressionDocument.Factory.parse(System.in);
			if(!doc.validate(xmlOptions)) {
				throw new ParseException("Expression violates the schema: " +
						errors.toString());
			} else {
				return AbstractValue.parse(doc.getExpression());
			}
		} catch(XmlException xe) {
			throw new ParseException("Error parsing expression: " +
					xe.getMessage());
		} catch(IOException ioe) {
			throw new ParseException("Error reading expression xml: " +
					ioe.getMessage());
		}

	}
}
