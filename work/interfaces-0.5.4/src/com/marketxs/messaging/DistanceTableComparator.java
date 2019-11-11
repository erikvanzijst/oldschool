package com.marketxs.messaging;

import java.util.Comparator;

/**
 * <P>
 * This class is used compare two entries from the distance table and
 * determine which should logically come first in the distance table. The more
 * fields of a host address match those of the local address, the higher it
 * comes in the distance table. <BR>
 * At runtime when a unicast packet comes in, its destination address is
 * matched with the entries in the distance table, starting at the top. As
 * soon as a match is found (this may be a wildcard), the search is stopped
 * and that routing entry is used for processing.
 * </P>
 * <P>
 * An example of a correctly sorted distance table for host A.B.C.D is:
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
 * @author Erik van Zijst - 17.sep.2003 - erik@marketxs.com
 */
public class DistanceTableComparator implements Comparator
{
	/**
	 * Entries in the distance table are ordered by destination address. This
	 * method compares the destination addresses of two entries from the
	 * distance table (both can contain wildcards) and determines which entry
	 * should logically come first.
	 * 
	 * @param o1 an instance of DistanceTable.
	 * @param o2 an instance of DistanceTable.
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
		 
		String[] fields1 = ((DistanceTable)o1).dest.getFields();
		String[] fields2 = ((DistanceTable)o2).dest.getFields();

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
