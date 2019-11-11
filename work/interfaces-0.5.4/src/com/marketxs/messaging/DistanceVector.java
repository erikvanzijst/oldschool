package com.marketxs.messaging;

import java.io.Serializable;

/**
 * <P>
 * This is the triplet that defines a routing table entry in ExBF.
 * </P>
 * 
 * @author Erik van Zijst - erik@marketxs.com
 * @version	0.1
 */
public class DistanceVector implements Serializable, Cloneable
{
	public static final int UNREACHABLE = 65535;
	public Address destination;
	public Address head;
	public int distance;

	public DistanceVector()
	{
		destination = new Address();
		head = new Address();
	}

	public DistanceVector(Address dest, Address head, int distance)
	{
		this.destination = dest;
		this.head = head;
		this.distance = distance;
	}
	
	public Object clone()
	{
		DistanceVector dv = null;
		try
		{
			dv = (DistanceVector)super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			// assert false;
		}
		
		dv.destination = (Address)destination.clone();
		dv.head = (Address)head.clone();
		
		return dv;
	}

	/**
	 * <P>
	 * Returns the distance vector as a string in the following format:
	 * </P>
	 * <code>(dest.address, head.of.path, 72)</code>
	 * 
	 * <P>
	 * N.B. <BR>
	 * reserved addresses are represented by "?" while unreachable distances
	 * are represented by the string "inf".
	 * </P>
	 */
	public String toString()
	{
		return new String("(" + destination.toString() + ", " + head.toString() + ", " + (distance == UNREACHABLE ? "inf" : String.valueOf(distance)) + ")");
	}
}
