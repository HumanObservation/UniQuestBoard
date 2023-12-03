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
$order_id = "";
if (isset($_POST["order_id"]) && !empty(trim($_POST["order_id"])))
{
    $order_id = mysqli_real_escape_string($connection, trim($_POST["order_id"]));
}
$update = "UPDATE orders SET status = 5 WHERE `order_id` = '".$order_id."';";

if(mysqli_query($connection, $update))
{
	echo "Update successfully!";
}
else 
{
    echo "Error in the query: " . mysqli_error($connection);
}

mysqli_close($connection);
exit();
?>