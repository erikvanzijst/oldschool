// BNV Online
// 04.01.99 icehawk@bart.nl


// TU Delft Datawarehouse project - BNV_CheckSyntax service class
// This class checks the syntax of an input field and if possible, fixes it.
// It is called from many database-messing classes.


class BNV_CheckSyntax
{

	public String checkOutlet(String outlet)
	{
		int nX, mX=0;
		String result = "error";
		String temp = "";

		outlet = outlet.toUpperCase().trim();	// rid trailing spaces

		// first filter out the alphanumeric characters only.. trash the rest..
		for(nX=0; nX<outlet.length(); nX++)
			if( ((outlet.charAt(nX) >= 'A') && (outlet.charAt(nX) <= 'Z')) || ((outlet.charAt(nX) >= '0') && (outlet.charAt(nX) <= '9')) || (outlet.charAt(nX) == '.') )
				temp = temp + outlet.substring(nX, nX+1);

		if(temp.length() != 7)
			return result;	// error string or only bogus data

		// Now check if it's letter-letter-2*number-dot-2*number
		for(nX=0; nX<2; nX++)
			if((temp.charAt(nX) < 'A') || (temp.charAt(nX) > 'Z'))	// the first 2 are not both letters
				return result;
		for(nX=2; nX<4; nX++)
			if((temp.charAt(nX) < '0') || (temp.charAt(nX) > '9'))	// the next are not both numbers
				return result;
		if(temp.charAt(4) != '.')
			return result;
		for(nX=5; nX<7; nX++)
			if((temp.charAt(nX) < '0') || (temp.charAt(nX) > '9'))	// the last are not both numbers
				return result;

		// OK, all was ok, the outlet string was fixed, or even uncorrupted
		result = temp;

		return result;
	}


	public String checkRoom(String room)
	{
		return room;
	}


	public String checkTelIP(String telIP)
	{
		return telIP;
	}

} // end of class
