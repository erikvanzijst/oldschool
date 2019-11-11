package com.marketxs.messaging;

import java.util.Comparator;
import com.marketxs.log.Log;

/**
 * <P>
 * This class compares two addresses (both can contain wildcards) and
 * determines which entry should logically come first. The more
 * fields of a host address match those of the local address, the higher it
 * comes in the distance table.
 * </P>
 * <P>
 * At runtime when a unicast packet comes in, its destination address is
 * matched with the entries in the distance table, starting at the top. As
 * soon as a match is found (this may be a wildcard), the search is stopped
 * and that routing entry is used for processing.
 * </P>
 * <P>
 * An example of a correctly sorted list of addresses is:
 * </P>
 * 
 * <PRE>
 * 		A.B.C.D
 * 		A.B.C.E
 * 		A.B.C.E.*
 * 		A.B.D.*
 * 		B
 * 		B.*
 * </PRE>
 * 
 * @author Erik van Zijst - erik@marketxs.com
 * @version	0.3, 08.dec.2003
 */
public class AddressComparator implements Comparator
{
	private Log logger = Log.instance(AddressComparator.class);
	
	/**
	 * This method compares two addresses (both can contain wildcards) and
	 * determines which entry should logically come first.
	 * 
	 * @param o1 an instance of Address.
	 * @param o2 an instance of Address.
	 * @return <LI>1 when o1 is less specific than o2 (for example A.B.C.*
	 * 	and A.B.C) or both are equally specific, but o1 lexicographically
	 * 	comes second (for example X.Y.Z and A.B.C);
	 * 	<LI>0 when o1 is equal to o2;
	 * 	<LI>-1 when o1 is more specific than o2 (for example A.B.C and A.B.*),
	 * 	or both are equally specific but o1 lexicographically comes first (for
	 *  example A.B.C and X.Y.Z).
	 */
	public int compare(Object o1, Object o2)
	{
		// these are defines, so we can easily change the order from
		// descending to ascending by flipping 1 and -1.
		final int MORE_SPECIFIC = -1;	// returned when o1 is a more specific address than o2
		final int EQUALLY_SPECIFIC = 0;	// returned when o1 is equally specific as o2
		final int LESS_SPECIFIC = 1;	// returned when o1 is a less specific address than o2
		
		String[] fields1 = null;
		String[] fields2 = null;
		
		try
		{
			fields1 = ((Address)o1).getFields();
			fields2 = ((Address)o2).getFields();
		}
		catch(ClassCastException e)
		{
			logger.log("ClassCastException while comparing addresses. o1 = " + o1.getClass().getName() + ", o2 = " + o2.getClass().getName(), Log.SEVERITY_ERROR);
			throw e;
		}

		/**
		 * A reserved address is least specific as it doesn't match a single
		 * address, so first run simple checks for reserved addresses.
		 */
		if(fields1 == null)
		{
			if(fields2 != null)
			{
				// o1 is reserved, while o2 is not
				return LESS_SPECIFIC;
			}
			else
			{
				// o1 and o2 are both reserved
				return EQUALLY_SPECIFIC;
			}
		}
		else
		{
			if(fields2 == null)
			{
				// o1 is not null, but o2 is
				return MORE_SPECIFIC;
			}
		}

		for(int nX = 0; nX < fields1.length && nX < fields2.length; nX++)
		{
			if(fields1[nX].equals("*"))
			{
				// a * is always the last character
				if(fields2[nX].equals("*"))
				{
					// A.B.* and A.B.*
					return EQUALLY_SPECIFIC;
				}
				else
				{
					// A.B.* and A.B.C or
					// A.B.* and A.B.C.D or
					// A.B.* and A.B.C.*
					return LESS_SPECIFIC; 
				}
			}
			else
			{
				if(fields2[nX].equals("*"))
				{
					// A.B.C and A.B.* or
					// A.B.C and A.*
					return MORE_SPECIFIC;
				}
				else
				{
					int cmp = fields1[nX].compareTo(fields2[nX]);
					
					if(cmp != 0)
					{
						return cmp;
					}
					else
					{
						continue;
					}
				}
			}
		}
		
		if(fields2.length == fields1.length)
		{
			// A.B.C and A.B.C
			return EQUALLY_SPECIFIC;
		}
		/**
		 * When the first part of string o2 equals all of string o1, we have 
		 * to decide which is more specific by looking at the rest of o2.
		 */
		else if(fields2.length > fields1.length)
		{
			if(fields2[fields2.length - 1].equals("*"))
			{
				// A.B.C and A.B.C.---.* (where --- is n more tokens)
				return MORE_SPECIFIC;
			}
			else
			{
				// A.B.C and A.B.C.--- (where --- is n more tokens)
				return LESS_SPECIFIC;
			}
		}
		/**
		 * When the first part of string o1 equals all of string o2, we have
		 * to decide which is more specific by looking at the rest of o1.
		 */
		else
		{
			if(fields1[fields1.length - 1].equals("*"))
			{
				// A.B.C.---.* and A.B.C
				return LESS_SPECIFIC; 
			}
			else
			{
				// A.B.C.--- and A.B.C
				return MORE_SPECIFIC;
			}
		}
	}
}
