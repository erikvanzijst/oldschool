<?
/*
 * array
 * (
 * 	"product_1 => array
 * 	(
 * 		name => "product_1"
 * 		instock => 4
 * 		sold => 1
 * 	)
 * 	"product_2 => array
 * 	(
 * 		name => "product_2"
 * 		instock => 6
 * 		sold => 12
 * 	)
 * )
 */

/*
 * This function takes a productname and returns the number of items we have in
 * stock. If the stocks database does not (yet) exist, 0 is returned. If there
 * was a database, but an error occured while reading it, FALSE is returned.
 * Returns -1 on error or the amount of items in stock.
 */
function getAmount($product)
{
	include 'setup.php';

	$stocks = getStocks();
	if($stocks == -1)
		return -1;
	else
	{
		if(isset($stocks[$product][instock]))
			return $stocks[$product][instock];
		else
			return 0;
	}
}

/*
 * Returns the new number in stock, -1 if there are too little (or none)
 * available, or -2 on error.
 */
function stockDecrement($product, $quantity)
{
	include 'setup.php';

	clearstatcache();
	if(file_exists($STOCKFILE) && is_readable($STOCKFILE) && filesize($STOCKFILE) > 0)
	{
		$handle = fopen($STOCKFILE, "r+");
		flock($handle, 2);	// get an exclusive lock

		$stocks = unserialize(fread($handle, filesize($STOCKFILE)));
		if($stocks == FALSE)
		{
			$retval = -2;	// error reading stocks
		}
		else
		{
			if(($stocks[$product][instock] - $quantity) < 0)
			{
				$retval = -1;	// not enough available
			}
			else
			{
				$stocks[$product][instock] -= $quantity;
				$retval = $stocks[$product][instock];
				$stocks[$product][sold] += $quantity;

				rewind($handle);
				if(fwrite($handle, serialize($stocks), strlen(serialize($stocks))) == FALSE)
					$retval = -2;	// error storing new stock amounts
			}
		}

		flock($handle, 3);	// release lock again
		fclose($handle);
	}
	else
		$retval = -1;

	return $retval;
}

/*
 * Returns -1 on error, 0 on success.
 */
function stockIncrement($product, $quantity)
{
	include 'setup.php';

	clearstatcache();
	if(file_exists($STOCKFILE) && is_readable($STOCKFILE) && filesize($STOCKFILE) > 0)
	{
		$handle = fopen($STOCKFILE, "r+");
		flock($handle, 2);	// get an exclusive lock

		$stocks = unserialize(fread($handle, filesize($STOCKFILE)));
		if($stocks == FALSE)
		{
			$retval = -1;	// error reading stocks
		}
		else
		{
			if(!isset($stocks[$product]))
			{
				// product does not exist
				return -1;
			}
			else
			{
				$stocks[$product][instock] += $quantity;

				rewind($handle);
				if(fwrite($handle, serialize($stocks), strlen(serialize($stocks))) == FALSE)
					$retval = -1;	// error storing new stock amounts
			}
		}

		flock($handle, 3);	// release lock again
		fclose($handle);
	}
	else
		$retval = -1;

	return $retval;
}

function getSold($product)
{
	include 'setup.php';

	$stocks = getStocks();
	if($stocks == -1)
		return -1;
	else
	{
		if(isset($stocks[$product][sold]))
			return $stocks[$product][sold];
		else
			return 0;
	}
}

/*
 * This function takes the stocks datastructure (1-dimensional array) and
 * serializes it to file.
 * Returns -1 on error, 0 otherwise.
 */
function setAmount($product, $instock)
{
	include 'setup.php';

	$stocks = getStocks();
	if($stocks == -1)
		return -1;

	if(isset($stocks[$product]))
		$stocks[$product][instock] = intval($instock);
	else
		$stocks[$product] = array(instock => intval($instock), sold => 0);

	$handle = fopen($STOCKFILE, "w");
	if($handle == FALSE)
	{
		return -1;
	}
	else
	{
		flock($handle, 2);	// get exclusive lock
		if(fwrite($handle, serialize($stocks), strlen(serialize($stocks))) == FALSE)
			$retval = -1;
		else
			$retval = 0;

		flock($handle, 3);	// release lock after write
		fclose($handle);
		return $retval;
	}
}

function getStocks()
{
	include 'setup.php';

	clearstatcache();
	if(file_exists($STOCKFILE) && is_readable($STOCKFILE) && filesize($STOCKFILE) > 0)
	{
		$handle = fopen($STOCKFILE, "r");
		flock($handle, 1);	// get shared (read) lock
		$stocks = unserialize(fread($handle, filesize($STOCKFILE)));
		flock($handle, 3);	// release lock again
		fclose($handle);

		if($stocks == FALSE)
		{
			return -1;
		}
		else
		{
			return $stocks;
		}
	}
	else
		// empty array
		return array( array() );
}

?>
