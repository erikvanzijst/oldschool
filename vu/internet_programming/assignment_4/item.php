<?
include 'presentation.php';
include 'db.php';

if($id > (count($inventory) - 1))
{
	$template_error = str_replace("__MESSAGE__", "Sorry, but that item is not in our database.", $template_error);
	print $template_error;
}
else
{
	$template_item = str_replace("__BIGIMAGE__", "images/".$inventory[$id]["bigimage"], $template_item);
	$template_item = str_replace("__PRICE__", $inventory[$id]["price"], $template_item);
	$template_item = str_replace("__DESC__", $inventory[$id]["desc"], $template_item);
	$template_item = str_replace("__NAME__", $inventory[$id]["name"], $template_item);
	$template_item = str_replace("__ID__", $id, $template_item);

	print $template_item;
}
?>
