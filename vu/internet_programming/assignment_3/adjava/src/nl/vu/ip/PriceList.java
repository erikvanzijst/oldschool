package nl.vu.ip;

import java.util.TreeMap;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;
import java.io.Reader;
import java.io.IOException;
import java.io.Serializable;
import java.io.BufferedReader;

/**
 * This class contains the AdServer's price configuration. Ad space is cheaper
 * when purchased in larger volumes at once. This class uses this information
 * to calculate the amount of ad space a client can purchase, given its budget.
 * <BR> Determining how much ad space can be purchased given a certain budget
 * is easily done by invoking the class' methods.
 * <P>
 * An instance of this class is returned by the AdServer's remote
 * <code>getPriceList()</code> method.
 * <P>
 *
 * @author	Erik van Zijst - erik@prutser.cx - 07.apr.2003
 */
public class PriceList implements Serializable
{
	private SortedMap prices = null;

	/**
	 * The PriceList's constructor takes a Reader object from which it
	 * reads its pricing configuration. <BR>
	 * The reader is typically a character file that looks like this:
	 * <P>
	 * <PRE>
	 * 	# Pricelist for the Java AdServer.
	 * 	#
	 * 	# The first parameter is the amount, the second the
	 * 	# total price for that amount of ads.
	 * 	
	 * 	1 = 1.0
	 * 	5 = 4.50
	 * 	10 = 7.50
	 *
	 * </PRE>
	 * <P>
	 *
	 * Note that the parser is rather picky and strict and assumes healthy
	 * configurations, i.e. descreasing price per ad for increased amounts
	 * of ads. The records in the file do not necessarily need to be
	 * ordered ascending.
	 *
	 * @param	reader a character reader that contains the pricing
	 * 	information.
	 */
	public PriceList(Reader reader)
	{
		prices = new TreeMap();
		BufferedReader br = new BufferedReader(reader);
		String line = null;

		try
		{
			// read one line at a time
			while( (line = br.readLine()) != null)
			{
				if(line.indexOf('#') == 0)	// allow for hashed comments
					continue;

				// parse each line using a StringTokenizer
				StringTokenizer strtok = new StringTokenizer(line, " =");

				try
				{
					int quantum = Integer.parseInt(strtok.nextToken());
					float price = Float.parseFloat(strtok.nextToken());
					prices.put(new Integer(quantum), new Float(price));
					System.out.println("Read price for " + quantum + " ads: " + price);
				}
				catch(NumberFormatException e)
				{
					// ignore valid syntax
				}
				catch(NoSuchElementException e)
				{
					// ignore valid syntax
				}
				catch(NullPointerException e)
				{
					// ignore valid syntax
				}
			}
		}
		catch(IOException e)
		{
			System.err.println("Error reading price configuration: " + e.getMessage());
		}
	}

	/**
	 * This method takes a budget and calculates the price per ad when the
	 * whole budget is spent on ad space. Usually, the higher the budget,
	 * the lower the price per ad. This method is used internally only.
	 * <P>
	 *
	 * @param	budget the client's budget it can spend on ad space.
	 * @return	the price per ad for this budget.
	 */
	private float getPricePerAd(float budget)
	{
		// return 0 if pricing information is missing
		if(prices.size() == 0)
			return 0;

		Iterator it = prices.keySet().iterator();

		// determine the lowest price possible for this budget
		float lowest = ((Float)prices.get( prices.firstKey() )).floatValue();
		while(it.hasNext())
		{
			Integer quantum = (Integer)it.next();
			float price = ((Float)prices.get(quantum)).floatValue();

			if(budget >= price)
				lowest = price / quantum.intValue();
			else
				break;
		}

		return lowest;
	}

	/**
	 * This method takes a budget and calculates how much ad space can be
	 * purchased with it, taking the discount into account. Returns 0 if
	 * there is no price information (i.e. misconfigured server).
	 *
	 * @param	budget the client's budget it can spend on ad space.
	 * @return	the amount of ad space that can be purchased with the
	 * 	supplied budget.
	 */
	public int getNumberOfAds(float budget)
	{
		float pricePerAd = getPricePerAd(budget);

		if(pricePerAd == 0)
			return 0;
		else
			return (int)(budget / pricePerAd);
	}
	
	/**
	 * This method calculates the total price for a given number of ads. It
	 * takes quantum discount into account. Usually, this method is used in
	 * combination with the return vanlue of the <code>getNumberOfAds()</code>
	 * method.
	 * <P>
	 *
	 * @param	numberOfAds the amount of ad space that is to be
	 * 	purchased.
	 * @return	the total price for the requested ad space.
	 */
	public float getPrice(int numberOfAds)
	{
		// return 0 if pricing information is missing
		if(prices.size() == 0 || numberOfAds == 0)
			return 0;

		int quantum = 0;
		Iterator it = prices.keySet().iterator();
		while(it.hasNext())
		{
			int _quantum = ((Integer)it.next()).intValue();
			if(numberOfAds >= _quantum)
				quantum = _quantum;
			else
				break;
		}
		
		// total price = number_of_ads * price_per_ad
		return numberOfAds * ( ((Float)prices.get(new Integer(quantum))).floatValue() / quantum );
	}
}
