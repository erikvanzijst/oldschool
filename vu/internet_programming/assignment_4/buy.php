<?
{
	include 'presentation.php';
	include 'stockmanip.php';
	include 'bankclient.php';
	include 'setup.php';
	include 'db.php';

	if(isset($account) && is_numeric($account) && isset($quantity) && is_numeric($quantity) && isset($id))
	{
		// purchase
		$_val = stockDecrement("product_$id", $quantity);	// reserve
		if($_val == -1)
			fail("Sorry, but we don't have enough in stock for that order.");
		else if($_val == -2)
			fail("Sorry, but there was an error while reserving the product for you.");
		else
		{
			$certificate = bank_pay($account, $BANK_ACCOUNT, ($quantity * $inventory[$id][price]));
			if($certificate == -1)
			{
				stockIncrement("product_$id", $quantity);	// un-reserve / rollback
				fail("Your payment failed. Please verify that your account has sufficient credit and that the account number is correct.");
			}

			$page = str_replace("__PRICE__", $inventory[$id][price], $template_buy_success);
			$page = str_replace("__AMOUNT__", ($quantity * $inventory[$id][price]), $page);
			$page = str_replace("__SRCACCOUNT__", $account, $page);
			$page = str_replace("__DESTACCOUNT__", $BANK_ACCOUNT, $page);
			$page = str_replace("__QUANTITY__", $quantity, $page);
			$page = str_replace("__CERTIFICATE__", $certificate, $page);
			
			print $page;
		}
	}
	else
	{
		// display
		if($id > (count($inventory) - 1))
		{
			$template_error = str_replace("__MESSAGE__", "Sorry, but that item is not in our database.", $template_error);
			print $template_error;
		}
		else
		{
			$template_buy = str_replace("__BIGIMAGE__", "images/".$inventory[$id][bigimage], $template_buy);
			$template_buy = str_replace("__PRICE__", $inventory[$id][price], $template_buy);
			$template_buy = str_replace("__NAME__", $inventory[$id][name], $template_buy);
			$template_buy = str_replace("__STOCK__", getAmount("product_$id"), $template_buy);
			$template_buy = str_replace("__ID__", $id, $template_buy);

			print $template_buy;
		}
	}
}

function fail($message)
{
	include 'presentation.php';

	exit(str_replace("__MESSAGE__", $message, $template_error));
}

?>
