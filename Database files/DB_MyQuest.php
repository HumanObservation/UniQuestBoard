<?php
require_once 'DB_config.php';
$config = new DB_config;
$connection = mysqli_connect($config->dbhost,$config->dbuser,
    $config->dbpass,$config->dbname);

if(mysqli_connect_errno())
{
    die("Error!! Database connection failed: ". mysqli_connect_error().
        " (". mysqli_connect_errno() . ") ");
}
?>

<?php
$itsc = "";
if (isset($_POST["publisher"]) && !empty(trim($_POST["publisher"])))
{
    $itsc = mysqli_real_escape_string($connection, trim($_POST["publisher"]));
}
$query =  "SELECT * FROM orders WHERE `publisher` = '".$itsc."';";
$result = mysqli_query($connection, $query);
$array = array();
$count = 1;
while($row = mysqli_fetch_array($result))
{
	$array[$count] = array(array("order_id" => $row[0], "user_id" => $row[1], "title" => $row[2], "description" => $row[3], "publisher" => $row[4], "publish_date" =>$row[5], "expired_date" =>$row[6], "contact" =>$row[7], "reward" => $row[8], "status" => $row[9]));
	$count++;
}
if($count != 1)
{
	echo json_encode($array);
}
else
{
	echo "The record is not found.";
}
mysqli_free_result($result);
mysqli_close($connection);
exit();
?>
