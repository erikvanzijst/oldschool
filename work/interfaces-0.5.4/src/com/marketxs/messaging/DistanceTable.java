package com.marketxs.messaging;

import com.marketxs.log.Log;

/**
 * <P>
 * An array of these items form a distance table.
 * </P>
 *
 * @author	Erik van Zijst - 29.aug.2002 - erik@marketxs.com
 */
public class DistanceTable implements Cloneable
{
	private static long staleTimeout = 300000l;	// 5-minute default
	public Address dest = new Address("");
	private int cost = DistanceVector.UNREACHABLE;
	public int hop = -1;	// index in dt, similar to cost_via and head_via
	public int[] cost_via = new int[Kernel.MAX_INTERFACES];
	public Address[] head_via = new Address[Kernel.MAX_INTERFACES];
	public long childlinks = 0;	// to support up to 64 interfaces
	public long timestamp = 0;	// timeout for unreachable entries

	static
	{
		try
		{
			staleTimeout = Integer.parseInt(Configurator.getProperty("com.marketxs.messaging.Kernel.staleTimeout", String.valueOf(staleTimeout)));
		}
		catch(Exception e)
		{
			Log.instance(DistanceTable.class).log("Unable to parse value for \"com.marketxs.messaging.Kernel.staleTimeout\".", e, Log.SEVERITY_WARNING);
		}
	}

	public DistanceTable()
	{
		for(int k = 0; k < Kernel.MAX_INTERFACES; k++)
		{
			cost_via[k] = DistanceVector.UNREACHABLE;
			head_via[k] = new Address("");	// RESERVED;
		}
	}
	
	public void setCost(int cost)
	{
		if(this.cost != DistanceVector.UNREACHABLE && cost == DistanceVector.UNREACHABLE)
		{
			// start the timer
			timestamp = System.currentTimeMillis();
		}
		this.cost = cost;
	}
	
	public int getCost()
	{
		return this.cost;
	}
	
	/**
	 * <P>
	 * Returns <code>true</code> if this entry was totally unreachable during
	 * the timeout interval, <code>false</code> otherwise.
	 * </P>
	 * 
	 * @return
	 */
	public boolean isStale()
	{
		return (cost == DistanceVector.UNREACHABLE && System.currentTimeMillis() - timestamp > staleTimeout);
	}
	
	/**
	 * <P>
	 * Returns a string representation of this distance table entry, formatted
	 * like this:
	 * <code>(j = a.b.d, pi = 1, sp = 63, cv = [71, 63, inf], hv = [a.b.c, x.y.z, ?])</code>
	 * where j is the destination address, pi is the index in the kernel's
	 * interface array that identifies the preferred interface for this
	 * destination, sp is the shortest path (cost), cv is the cost_via
	 * array with cost metric via other interfaces and hv is the head_via
	 * array showing the head nodes of the paths when going via the other
	 * interfaces. When a node is unreachable, the cost is represented by
	 * "inf" (infinite).
	 * </P>
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("(j = " + dest.toString() +
			", pi = " + hop +
			", sp = " + (cost == DistanceVector.UNREACHABLE ? "inf" : String.valueOf(cost)) +
			", cv = [");
			
		for(int nX = 0; nX < cost_via.length; nX++)
		{
			sb.append((cost_via[nX] == DistanceVector.UNREACHABLE ? "inf" : String.valueOf(cost_via[nX])));
			if(nX < cost_via.length - 1)
				sb.append(", ");
		}
		sb.append("], hv = [");
			
		for(int nX = 0; nX < head_via.length; nX++)
		{
			sb.append(head_via[nX].toString());
			if(nX < head_via.length - 1)
				sb.append(", ");
		}
		sb.append("])");
		
		return sb.toString();
	}
	
	/**
	 * <P>
	 * Returns a deep-copy of this DistanceTable instance. This method is used
	 * by the Kernel class when it returns a copy of the distance table
	 * datastructure to the user application to ensure a complete deep-copy of
	 * the distance table is returned and the user application cannot alter
	 * the table. This method relies on the fact that the Address class is
	 * cloneable.
	 * </P>
	 * 
	 * @return	a deep-copy of the DistanceTable entry.
	 */
	public Object clone()
	{
		DistanceTable dt = null;
		
		try
		{
			dt = (DistanceTable)super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			// assert false;
		}
		
		dt.dest = (Address)dest.clone();
		dt.hop = hop;
		
		dt.head_via = new Address[head_via.length];
		for(int nX = 0; nX < head_via.length; nX++)
			dt.head_via[nX] = (Address)head_via[nX].clone();
		
		return dt;
	}
}
