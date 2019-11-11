package com.marketxs.messaging;

import java.util.StringTokenizer;

/**
 * <P>
 * This class represents a dotted-string address that can be used to identify
 * a unicast (address of a router or host), multicast or broadcast address.
 * </P>
 * <P>
 * In this version, a special bit is used to distinguish between unicast and
 * multi- broadcast addresses.
 * </P>
 * Examples of valid addresses
 * are:
 * </P>
 * 
 * <LI>some.address.to.test05
 * <LI>myAddress
 * 
 * <P>
 * Incorrect addresses include:
 * </P>
 * 
 * <LI>myAddress.
 * <LI>empty..field
 * <LI>.
 * <LI>no. spaces.allowed
 * 
 * <P>
 * A special address is the RESERVED address that is sometimes used in
 * distance vectors and as initial values for the preferred hop in a distance
 * table. It is represented by an empty string ("").
 * </P>
 * 
 * @location	api
 * @author Erik van Zijst - 22.oct.2003 - erik@marketxs.com
 */
public class Address implements Comparable, Cloneable
{
	public static final int MAXLENGTH = 127;	// the length field on the wire is only 7 bits
	private boolean isMulticast = false;
	private String[] fields = null;
	private String _address = "";
	
	public static final Address RESERVED = new Address("");
	
	public Address()
	{
	}
	
	/**
	 * <P>
	 * Creates a new Address instance for the given address string.
	 * To create a reserved address (that can be used as initial values in the
	 * distance table, pass an empty string ("") to the constructor or call
	 * setReserved() on any address instance to convert it into reserved.
	 * </P>
	 * <P>
	 * Note that passing <code>null</code> to the constructor is no longer
	 * supported and now throws a <code>NullPointerException</code>.
	 * </P>
	 * 
	 * @param addr
	 * @throws InvalidAddressException
	 */
	public Address(String addr) throws InvalidAddressException
	{
		init(addr);
		setMulticast(false);
	}
	
	/**
	 * <P>
	 * Creates a new multicast or unicast Address instance for the given
	 * address string.
	 * </P>
	 * 
	 * @param addr
	 * @param multicast	use true to make this a multicast address, or false to
	 * 	make it unicast.
	 * @throws InvalidAddressException
	 */
	public Address(String addr, boolean multicast) throws InvalidAddressException
	{
		init(addr);
		setMulticast(multicast);
	}
	
	private void init(String addr) throws InvalidAddressException
	{
		if(addr.length() == 0)
		{
			setReserved();
		}
		else if(!validate(addr))
		{
			throw new InvalidAddressException("Address (" + addr + ") syntactically incorrect.");
		}
		else
		{
			_address = addr.toLowerCase();
			fields = null;
		}
	}
	
	/**
	 * <P>
	 * Takes the raw bytes of a packet and starts extracting the address
	 * information at the pos index. After parsing, it returns the position of
	 * the last byte it read.
	 * </P>
	 * The layout of an address field is depicted below: <BR>
	 * 
	 *  <PRE>
	 *  0                   1                   2                   3
	 *  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
	 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
	 * |M|   length    |     dotted address string (max 127 bytes)     |
	 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
	 * </PRE>
	 * <P>
	 * The first bit indicates whether or not this is a multicast address or
	 * not. If the bit is set, the address is multicast, if it is unset, it is
	 * unicast.
	 * </P>
	 * <P>
	 * N.B.<BR>
	 * There is a special case for reserved addresses. These are sometimes
	 * used in distance vectors and initially in distance tables. When an
	 * address is reserved, it is encoded with length = 0 and no further
	 * address content.
	 * </P>
	 * 
	 * @param raw
	 * @param pos	position to start parsing.
	 * @return	the position of the last byte that was parsed.
	 */
	public int parseAddress(byte[] raw, int pos) throws DeserializeException
	{
		try
		{
				byte b = raw[pos];
				if(((b >> 7) & 1) == 1)	// test if the first bit is set
				{
					isMulticast = true;
					b = (byte)((int)b & 127);
				}
				else
					isMulticast = false;
					
				if(b == 0)
				{
					setReserved();
				}
				else
				{
					byte[] _addr = new byte[b];
					System.arraycopy(raw, pos+1, _addr, 0, b);
					
					String __addr = new String(_addr);
					if(validate(__addr))
					{
						_address = __addr;
						fields = null;
					}
					else
						throw new DeserializeException("Packet contains illegal address string.");
				}

				return pos + b;
		}
		catch(IndexOutOfBoundsException e)
		{
			throw new DeserializeException("Incorrect address length byte.");
		}
	}
	
	/**
	 * <P>
	 * Encodes the address in binary according to the following format:
	 * </P>
	 * 
	 *  <PRE>
	 *  0                   1                   2                   3
	 *  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
	 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
	 * |M|   length    |     dotted address string (max 127 bytes)     |
	 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
	 * </PRE>
	 * <P>
	 * The first bit indicates whether or not this is a multicast address.
	 * If the bit is set, the address is multicast, if it is unset, it is
	 * unicast.
	 * </P>
	 * <P>
	 * N.B.<BR>
	 * There is a special case for reserved addresses. These are sometimes
	 * used in distance vectors and initially in distance tables. When an
	 * address is reserved, it is encoded with length = 0 and no further
	 * address content.
	 * </P>
	 * 
	 * @return	the binary encoded address that can be used in packet headers.
	 */
	public byte[] serialize()
	{
		byte[] raw = new byte[1 + _address.length()];
		
		if(isMulticast())
			raw[0] = (byte)(_address.length() | 128);	// set the first bit
		else
			raw[0] = (byte)(_address.length() & 127);	// unset the first bit
			
		System.arraycopy(_address.getBytes(), 0, raw, 1, _address.length());
		
		return raw;
	}
	
	/**
	 * <P>
	 * Validates an address for correct syntax. Examples of valid addresses
	 * are:
	 * </P>
	 * 
	 * <LI>some.address.to.test05
	 * <LI>myAddress
	 * 
	 * <P>
	 * Incorrect addresses include:
	 * </P>
	 * 
	 * <LI>myAddress.
	 * <LI>empty..field
	 * <LI>.
	 * <LI>no. spaces.allowed
	 * 
	 * @return	true if the address is syntactically correct, false otherwise.
	 */
	public static boolean validate(String addr)
	{
		char[] chars = addr.toCharArray();
		boolean previousWasDot = true;
		
		if(chars[0] == '.' || chars[chars.length -1] == '.' || addr.length() > MAXLENGTH)
			return false;
			
		for(int nX = 0; nX < chars.length; nX++)
		{
			if(Character.isLetterOrDigit(chars[nX]))
			{
				previousWasDot = false;
			}
			else if(chars[nX] == '.')
			{
				if(previousWasDot)
					return false;
				else
					previousWasDot = true;
			}
			else if(chars[nX] == '*' && nX == (chars.length - 1) && previousWasDot)
			{
				// last field is a *
				previousWasDot = false;
			}
			else
			{
				// illegal character encountered
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * <P>
	 * This method summarizes the current address against a given non-wildcard
	 * address. This is method is called on incoming address instances in
	 * routing updates with the node's local address to summarize against.
	 * When the node's address is A.B.C.D and is used to summarize address
	 * A.B.X.Y, it returns A.B.X.*
	 * </P>
	 * <P>
	 * More formal: (new Address("A.B.X.Y")).summarize(new Address("A.B.C.D")).equals(new Address("A.B.X.*")) == true 
	 * </P>
	 * <P>
	 * Note that a reserved address cannot be summarized and will yield
	 * another reserved instance.
	 * </P>
	 * 
	 * @param address a non-wildcard address against which to summarize this
	 * 	address instance.
	 * @return the summarized address, often a wildcard. This is always a new
	 * 	instance. Returns reserved is this instance is reserved.
	 */
	public Address summarize(Address address)
	{
		String[] ref = address.getFields();
		String[] us = getFields();
		StringBuffer sb = new StringBuffer();	// used to store the new summarized address
		Address retaddr = new Address();	// address that will be returned
		
		// sanity check
		if(isReserved())
			return new Address("");
			
		for(int nX = 0; nX < ref.length; nX++)
		{
			if(us.length <= nX)
			{
				// we are out of fields already, but everything was equal
				retaddr._address = _address;
				return retaddr;
			}
			
			sb.append( (nX > 0 ? "." : "") + us[nX]);

			if(!ref[nX].equals(us[nX]))
			{
				// if "us" has more fields, replace them by * and return
				if(us.length > (nX + 1))
				{
					sb.append(".*");
				}
				retaddr._address = sb.toString();
				return retaddr;
			}
		}
		
		if(us.length == ref.length || us.length == (ref.length + 1))
		{
			// ref = A.B.C, us = A.B.C.D
			retaddr._address = _address;
			return retaddr;
		}
		else
		{
			// ref = A.B.C, us = A.B.C.D.E
			sb.append("." + us[ref.length] + ".*");
			retaddr._address = sb.toString();
			return retaddr;
		}
	}

	/**
	 * <P>
	 * Checks to see if this address instance (usually a wildcard) matches the
	 * specified address. For example, if this address contains "A.B.*", then
	 * specifying "X.Y.Z" or "A.*" will not match, while "A.B.C" or "A.B.C.*"
	 * will match. <BR>
	 * If this address is not a wildcard, it will only match itself.
	 * </P>
	 * <P>
	 * This method is used to find a route in the distance table that applies
	 * to the given address. In this case, the destination address in a packet
	 * is matched with each destination address (or wildcard) in the distance
	 * table until a match is found.
	 * </P>
	 * 
	 * @param address the address that will be matched with this instance.
	 * @return true if this address matches the supplied one, i.e. the given
	 * 	address is contained in this address.
	 */
	public boolean matches(Address address)
	{
		String[] addrFields = address.getFields();
		String[] ourFields = getFields();
		
		if(address == null || address.isReserved())
			return false;
			
		for(int nX = 0; nX < addrFields.length; nX++)
		{
			if(nX == ourFields.length)
			{
				// Our local address is out of fields and it didn't end in a
				// wildcard. Since the given address has at least one field
				// left, it cannot be matched.
				return false;
			}
				
			if(ourFields[nX].equals("*"))
			{
				// Our address ends in a wildcard while all previous fields
				// were the same.
				return true;
			}
				
			if(!addrFields[nX].equals(ourFields[nX]))
				return false;
		}
		
		if(ourFields.length > addrFields.length)
		{
			// Our address has all of the given address's fields, but ours is
			// longer and is therefore more specific.
			return false;
		}
		else
		{
			// all fields were equal
			return true;
		}
	}
	
	/**
	 * 
	 * @return true is this is a reserved address, false if not.
	 */
	public boolean isReserved()
	{
		return 0 == _address.length();
	}
	
	public void setReserved()
	{
		/**
		 * Internally an empty address string (0 characters) is used to
		 * identify a reserved address.
		 */
		_address = "";
		fields = null;
	}

	public boolean isMulticast()
	{
		return isMulticast;
	}
	
	/**
	 * <P>
	 * Specifies whether or not this address is a multicast address or a
	 * unicast address used to identify a specific host or router.
	 * </P>
	 * 
	 * @param multicast
	 */
	public void setMulticast(boolean multicast)
	{
		this.isMulticast = multicast;
	}
	
	/**
	 * <P>
	 * Compares this address instance with another. Returns true if the dotted
	 * address strings are equals (case insensitive, because address strings
	 * are always converted to lowercase first).
	 * </P>
	 */
	public boolean equals(Object o)
	{
		return ((Address)o)._address.equals(_address);
	}
	
	/**
	 * <P>
	 * Addresses are effectively normal strings with specific syntax rules, so
	 * we can rely on the comparable implementation of the String class.
	 * </P>
	 */
	public int compareTo(Object o)
	{
		return ((Address)o)._address.compareTo(_address);
	}
	
	/**
	 * <P>
	 * Because an Address is essentially a String class with symantic rules,
	 * we can use the hascode implementation of the String class.
	 * </P>
	 */
	public int hashCode()
	{
		return _address.hashCode();
	}
	
	public Object clone()
	{
		Address a = null;

		try
		{
			a = (Address)super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			// assert false;
		}
		
		return a;
	}
	
	/**
	 * <P>
	 * Returns the individual fields of the address as an array of strings.
	 * If the address's last field is a wildcard (*), it is also returned as a
	 * field.
	 * </P>
	 * 
	 * @return the fields of the address as an array of strings, or null if
	 * 	this is a reserved address.
	 */
	public String[] getFields()
	{
		if(!isReserved() && fields == null)
		{
			StringTokenizer strtok = new StringTokenizer(_address, ".");
			fields = new String[strtok.countTokens()];
			
			for(int nX = 0; strtok.hasMoreTokens(); nX++)
			{
				fields[nX] = strtok.nextToken();
			}
		
		}
		
		return fields;
	}
	
	/**
	 * Returns the string representation of the address.
	 */
	public String toString()
	{
		return isReserved() ? "?" : _address; 
	}
}
