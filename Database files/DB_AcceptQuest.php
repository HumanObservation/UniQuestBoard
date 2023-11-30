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
$itsc = "";
$order_id = "";
if (isset($_POST["itsc"]) && !empty(trim($_POST["itsc"])))
{
    $itsc = mysqli_real_escape_string($connection, trim($_POST["itsc"]));
}
if (isset($_POST["order_id"]) && !empty(trim($_POST["order_id"])))
{
    $order_id = mysqli_real_escape_string($connection, trim($_POST["order_id"]));
}

$query =  "SELECT `user_id` FROM user WHERE `itsc` = '".$itsc."';";
$result = mysqli_query($connection, $query);
if($row = mysqli_fetch_assoc($result))
{
	$user_id = $row['user_id'];
	$insert = "INSERT INTO quest (`order_id`, `user_id`) VALUES ('".$order_id."', '".$user_id."')";
	if (mysqli_query($connection, $insert)) 
	{
		$update = "UPDATE orders SET status = 1 WHERE `order_id` = '".$order_id."';";
		if(mysqli_query($connection, $update))
		{
			echo "Update successfully!";
		}
	    echo "Data inserted successfully!";
	} 
	else 
	{
	    echo "Error in the query: " . mysqli_error($connection);
	}
}
mysqli_free_result($result);
mysqli_close($connection);
exit();
?>