<?

define ("COMMAND_BANK_PAY", 1);
define ("COMMAND_BANK_CHECK", 2);
define ("ETX", chr(3));

/**
 * This method is used to transfer money from one bank account to the
 * other. It takes the location information of the bank server, as well
 * as the accounts and amount to transfer and sends a serializes
 * message to the bank using TCP sockets. <BR>
 * Returns the certificate of the money transfer.
 */
function bank_pay($src, $dest, $amount)
{
	$command = array(cmd => COMMAND_BANK_PAY, src => $src, dest => $dest, amount => $amount, cert => 0, retval => 0);
	$message = bank_send($command);

	return $message[retval];
}

/**
 * This method is used to verify an earlier money transfer. Given all
 * the transaction's properties such as bank accounts and amount,
 * together with the transaction certificate that the bank generated,
 * this method contacts the bank and lets the bank verify whether the
 * described transaction was indeed executed earlier. <BR>
 */
function bank_check($src, $dest, $amount, $certificate)
{
	// intentionally unimplemented
}

/*
 * This method takes a message, sends it to the bank server and returnes the
 * bank's response message.
 * A message is an associative array that looks like this:
 *
 * array (	cmd => COMMAND_BANK_PAY,
 * 		src => 123,
 * 		dest => 555,
 * 		amount => 12.5,
 * 		cert => 1551674815,
 * 		retval => 0);
 *
 * On error, a message is returned with the retval element set to -1.
 */
function bank_send($m)
{
	include 'setup.php';	// for the bank's address

	$res[retval] = -1;
	$fp = fsockopen($BANK_SERVER_ADDR, intval($BANK_SERVER_PORT), $errno, $errstr);
	if(!$fp)
	{
		print "$errstr. Is the bank server running at $BANK_SERVER_ADDR:$BANK_SERVER_PORT?<BR>\n";
	}
	else
	{
		$raw = $m[cmd]."|".$m[src]."|".$m[dest]."|".$m[amount]."|".$m[cert]."|".$m[retval].ETX;
		fputs($fp, $raw);

		fflush($fp);

		// read reply up to ETX of EOF
		while(!feof($fp) && ($_char = fgetc($fp)) != ETX)
		{
			$buf = $buf.$_char;
		}
		fclose($fp);

		// parsing the response is quite equal the C and Java code
		$field = 0;
		$tokens = explode("|", $buf);
		foreach($tokens as $tok)
		{
			switch($field)
			{
				case 0:
					$res[cmd] = intval($tok);
					break;
				case 1:
					$res[src] = intval($tok);
					break;
				case 2:
					$res[dest] = intval($tok);
					break;
				case 3:
					$res[amount] = $tok;
					break;
				case 4:
					$res[cert] = $tok;
					break;
				case 5:
					$res[retval] = $tok;
					break;
				default:
					break;
			}
			$field++;
			$tok = strtok("|");
		}

		return $res;
	}
}

?>
