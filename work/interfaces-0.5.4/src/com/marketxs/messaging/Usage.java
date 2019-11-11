package com.marketxs.messaging;

/**
 * This class is the default class that is run when the mxs-messaging jar file
 * is started with java -jar. It prints the available utility programs that
 * are in the jar file.
 * 
 * @author Erik van Zijst - erik@marketxs.com - 13.nov.2003
 */
public class Usage
{
	public static void main(String[] args)
	{
		String usage = "\n\tMarketXS Wide-Area Messaging Library.\n\n" +
			"This jar file contains 3 example programs that can be used to build an overlay network " +
			"and send messages across.\n\n" +
			"\tSimple Router\n" +
			"\t\t$ java -classpath messaging.jar:management-1.2.jar com.marketxs.messaging.SimpleRouter --help\n" +
			"\tThe SimpleRouter is an application that can be used to deploy a router daemon.\n\n" +
			"\tSimpleSink\n" +
			"\t\t$ java -classpath messaging.jar:management-1.2.jar com.marketxs.messaging.SimpleSink --help\n" +
			"\tThe SimpleSink is a program that can subscribes to one or more addresses and\n" +
			"\tsimply prints the contents of any received message." +
			"\n\n" +
			"\tSimpleSource\n" +
			"\t\t$ java -classpath messaging.jar:management-1.2.jar com.marketxs.messaging.SimpleSource --help\n" +
			"\tThe SimpleSource is a program that reads text from stdin and publishes each\n" +
			"\tline as a separate message to specified addresses.\n";
			
		System.out.println(usage);
	}
}
