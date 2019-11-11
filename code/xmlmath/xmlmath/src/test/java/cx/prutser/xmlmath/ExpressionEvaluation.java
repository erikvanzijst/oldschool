package cx.prutser.xmlmath;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.xmlbeans.XmlOptions;

import cx.prutser.xmlmath.operands.AbstractBooleanValue;
import cx.prutser.xmlmath.operands.AbstractDoubleValue;
import cx.prutser.xmlmath.operands.AbstractLongValue;
import cx.prutser.xmlmath.operands.AbstractValue;
import cx.prutser.xmlmath.operands.EvaluationException;
import cx.prutser.xmlmath.operands.ParseException;
import cx.prutser.xmlmath.schemas.TestDocument;
import cx.prutser.xmlmath.schemas.TestExceptionDocument.TestException;
import cx.prutser.xmlmath.schemas.TestDoubleDocument.TestDouble;
import cx.prutser.xmlmath.schemas.TestLongDocument.TestLong;
import cx.prutser.xmlmath.schemas.TestBooleanDocument.TestBoolean;

import junit.framework.TestCase;

/**
 * Base class for test cases that test validity of expressions. Subclasses can
 * be empty. This base class will look at the name of the subclass and look
 * for a corresponding xml file (resources/SubClassName.xml) through the
 * classloader.
 *
 * @author	Erik van Zijst - erik@marketxs.com
 * @version	1.0, 11.mar.2006
 */
public abstract class ExpressionEvaluation extends TestCase {

	private XmlOptions xmlOptions = null;

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(ExpressionEvaluation.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		xmlOptions = new XmlOptions();
		xmlOptions.setLoadLineNumbers();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	private String getFilename() {
		
		String classname = this.getClass().getName();
		return "/resources/" + classname.substring(classname
				.lastIndexOf('.') + 1) + ".xml";
	}

	public void testConstraintEvaluation() {

		System.out.println("Testing " + getFilename() + "...");
		Collection errors = new ArrayList();
		xmlOptions.setErrorListener(errors);

		TestDocument doc = null;
		try {
			doc = TestDocument.Factory.parse(getClass()
					.getResourceAsStream(getFilename()));
		} catch(IOException e) {
			e.printStackTrace();
			fail("XML file not found for this test.");
		} catch(Exception e) {
			e.printStackTrace();
			fail("Error parsing file " + getFilename() + ": " + e.getMessage());
		}
		
		if(!doc.validate(xmlOptions)) {
			fail("XML file is not valid: " + errors.toString());
		}
		TestDocument.Test test = doc.getTest();

		try {
			if(test.getTestBoolean() != null) {
				
				TestBoolean boolTest = test.getTestBoolean();
				
				AbstractBooleanValue booleanValue =
					(AbstractBooleanValue)AbstractValue.parse( boolTest.getAbstractBoolean() );
				assertEquals("The result of the expression was incorrect.",
						boolTest.getResult(), (boolean)booleanValue.getBoolean());
				
			} else if(test.getTestLong() != null) {
				
				TestLong longTest = test.getTestLong();
				AbstractLongValue longValue =
					(AbstractLongValue)AbstractValue.parse( longTest.getAbstractLong() );
				assertEquals("The result of the expression was incorrect.", longTest.getResult(), (long)longValue.getLong());
				
			} else if(test.getTestDouble() != null) {
				
				TestDouble doubleTest = test.getTestDouble();
				AbstractDoubleValue doubleValue =
					(AbstractDoubleValue)AbstractValue.parse( doubleTest.getAbstractDouble() );
				assertEquals("The result of the expression was incorrect.", doubleTest.getResult(), (double)doubleValue.getDouble());
				
			} else if(test.getTestException() != null) {
				TestException exceptionTest = test.getTestException();
				AbstractValue exp = AbstractValue.parse(exceptionTest.getValue());
				try {
					Object result = exp.getValue();
					fail("Invalid expression did not raise an exception. " +
							"Result: " + result.toString());
				} catch(EvaluationException ee) {
				}
			}
		} catch(ParseException e) {
			e.printStackTrace();
			fail("Unable to parse the expression tree: " + e.getMessage());
		}
	}
}
