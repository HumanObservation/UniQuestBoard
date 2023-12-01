<?php
require_once 'DB_config.php';
$config = new DB_config;
$connection = mysqli_connect($config->dbhost,$config->dbuser,
    $config->dbpass,$config->dbname);

if(mysqli_connect_errno())
{
    die("Error!! Database connection failed: ". mysqli_connect_error().
        " (". mysqli_connect_errno() . ") "
        );
}
?>

<?php
$id = "";
$array = array();
$count = 1;
if (isset($_POST["order_id"]) && !empty(trim($_POST["order_id"])))
{
    $id = mysqli_real_escape_string($connection, trim($_POST["order_id"]));
}
$query =  "SELECT * FROM orders WHERE `order_id` = '".$id."';";
$result = mysqli_query($connection, $query);
while($row = mysqli_fetch_array($result))
{
	$idquery = "SELECT itsc FROM user WHERE user_id = (SELECT user_id FROM quest WHERE order_id  = '".$id."');";
	$idresult = mysqli_query($connection, $idquery);
	if (mysqli_num_rows($idresult) > 0) 
	{
		while($idrow = mysqli_fetch_array($idresult))
		{
			$array[$count] = array(array("order_id" => $row[0], "user_id" => $row[1], "title" => $row[2], "description" => $row[3], "publisher" => $row[4], "publish_date" =>$row[5], "expired_date" =>$row[6], "contact" =>$row[7], "reward" => $row[8], "status" => $row[9], "taker" => $idrow[0]));
		}
	}
	else
	{
		$array[$count] = array(array("order_id" => $row[0], "user_id" => $row[1], "title" => $row[2], "description" => $row[3], "publisher" => $row[4], "publish_date" =>$row[5], "expired_date" =>$row[6], "contact" =>$row[7], "reward" => $row[8], "status" => $row[9]));			
	}
	
	$count++;
}
echo json_encode($array);
mysqli_free_result($result);
mysqli_close($connection);
exit();
?>
