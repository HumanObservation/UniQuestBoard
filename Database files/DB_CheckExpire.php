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

$query =  "SELECT `order_id` ,`publish_date`, `expired_date` FROM orders;";
$result = mysqli_query($connection, $query);
while($row = mysqli_fetch_array($result))
{
	$date1 = DateTime::createFromFormat("Y/m/d H:i", $row[1]);
	$date2 = DateTime::createFromFormat("Y/m/d H:i", $row[2]);
	if($date2 < $date1)
	{
		$update = "UPDATE orders SET status = 4 WHERE `order_id` = '".$row[0]."';";

		if(mysqli_query($connection, $update))
		{
			echo "Update successfully!";
		}
		else 
		{
		    echo "Error in the query: " . mysqli_error($connection);
		}
	}
}

mysqli_free_result($result);
mysqli_close($connection);
exit();
?>