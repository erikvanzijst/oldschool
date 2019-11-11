<?
{
	include '../db.php';
	include '../stockmanip.php';
	include '../presentation.php';

	// store the new stock amounts
	foreach(array_keys($HTTP_GET_VARS) as $_var)
	{
		if(strpos($_var, "product_") == 0)
			if(setAmount($_var, $HTTP_GET_VARS[$_var]) == -1)
				fail("There was an error saving the stock database. Make sure the stock database file as specified in setup.php is writable by the webserver.");
	}

	$list = "";
	$i = 0;
	foreach ($inventory as $_item)
	{
		$amount = getAmount("product_$i");
		$sold = getSold("product_$i");
		if($amount == -1 || $sold == -1)
			fail("There was an error reading the stock database for product \".$_item[name].\".");

		$stock_item = str_replace("__SMALLIMAGE__", "../images/".$_item["smallimage"], $template_admin_item);
		$stock_item = str_replace("__NAME__", $_item["name"], $stock_item);
		$stock_item = str_replace("__ID__", "product_".$i, $stock_item);
		$stock_item = str_replace("__AMOUNT__", $amount, $stock_item);
		$stock_item = str_replace("__SOLD__", $sold, $stock_item);

		$list = $list.$stock_item;
		$i++;
	}

	exit(str_replace("__LIST__", $list, $template_admin));
}

function fail($message)
{
	include '../presentation.php';

	exit(str_replace("__MESSAGE__", $message, $template_error));
}

?>
