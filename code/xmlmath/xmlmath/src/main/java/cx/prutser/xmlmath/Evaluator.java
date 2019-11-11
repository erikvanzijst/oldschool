package cx.prutser.xmlmath;

/**
 * Reads an xml file with an expression from stdin and prints the result.<br/>
 * The input xml is validated against the <tt>xmlmath.xsd</tt> schema file.
 * Below is a sample of a valid expression document:
 * <P>
 * <PRE>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;
 * &lt;sin xmlns="http://prutser.cx/xmlmath/schemas"&gt;
 *   &lt;toDouble&gt;
 *     &lt;product&gt;
 *       &lt;pi/&gt;
 *       &lt;double value="2.0"/&gt;
 *     &lt;/product&gt;
 *   &lt;/toDouble&gt;
 * &lt;/sin&gt;
 * </PRE>
 * <P>
 * When this expression is run through the evaluator it yields the following
 * result:
 * <P>
 * <PRE>
 * $ java -jar dist/xmlmath-0.1.jar < exp1.xml
 * -2.4492935982947064E-16
 * $
 * </PRE>
 * Which means that when using double-precision floating point arithmetic, the
 * result of <tt>sin(2*pi)</tt> is very close to zero.
 *
 * @author	Erik van Zijst - erik@prutser.cx
 * @version	1.0, 12.mar.2006
 */
public class Evaluator {

	private static boolean stacktraces = false;
	
	public static void main(String... args) {
	
		parseArgs(args);
		try {
			System.out.println(
					ExpressionFactory.parseExpression(System.in).getValue());
		} catch(Exception re) {
			if(stacktraces) {
				re.printStackTrace();
			} else {
				System.err.println(re.getMessage());
			}
		}
	}
	
	private static void parseArgs(String... args) {
		
		final String usage = "java -jar xmlmath.jar [OPTION]\n" +
				"  XMLMath reads the expression from stdin.\n" +
				"	--help\n" +
				"		print this message" +
				"	-e\n" +
				"		print full tracktrace on error";

		for (String arg : args) {
			if(arg.equals("--help")) {
				System.out.println(usage);
				System.exit(0);
			} else if(arg.equals("-e")) {
				stacktraces = true;
			}
		}
	}
}
