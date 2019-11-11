// BNV Online, Datawarehouse applicatie
// Erik van Zijst, 17.12.98 icehawk@xs4all.nl

// TU Delft BNV project - Floor-extract-O-matic service class
// This application is used internally to determine what floor an outlet is on.
// No servlet, not accessible to the outside world.

class BNV_FloorExtractor
{

	public int findFloor(String outlet)
	{
		int floor	= -2;	// -2 is error value
		outlet.trim();
		char outletChar[] = outlet.toCharArray();

		if( outletChar[1] != '.' )
			return floor;

		if( outletChar[0] == 'K' ) floor = -1;
		if( outletChar[0] == '0' ) floor = 0;
		if( outletChar[0] == '1' ) floor = 1;
		if( outletChar[0] == '2' ) floor = 2;
		if( outletChar[0] == '3' ) floor = 3;
		if( outletChar[0] == '4' ) floor = 4;
		if( outletChar[0] == '5' ) floor = 5;
		if( outletChar[0] == '6' ) floor = 6;

		return floor;
	}
} // end of class
