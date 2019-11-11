<?
include 'presentation.php';
include 'db.php';

$list = "";
$i = 0;
foreach ($inventory as $_item)
{
	$stock_item = str_replace("__SMALLIMAGE__", "images/".$_item["smallimage"], $template_list_item);
	$stock_item = str_replace("__NAME__", $_item["name"], $stock_item);
	$stock_item = str_replace("__PRICE__", $_item["price"], $stock_item);
	$stock_item = str_replace("__ITEMLINK__", "item.php?id=$i", $stock_item);

	$list = $list.$stock_item;
	$i++;
}

$page = str_replace("__LIST__", $list, $template_list);
print $page;
?>
