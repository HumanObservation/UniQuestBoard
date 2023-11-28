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
$array = array();
$count = 1;
if (isset($_POST["itsc"]) && !empty(trim($_POST["itsc"])))
{
    $itsc = mysqli_real_escape_string($connection, trim($_POST["itsc"]));
}
$query = "SELECT u.`user_id`, q.`order_id`, o.*
FROM user u
JOIN quest q ON u.`user_id` = q.`user_id`
JOIN orders o ON q.`order_id` = o.`order_id`
WHERE u.`itsc` = '".$itsc."';";
$result = mysqli_query($connection, $query);

while($row = mysqli_fetch_array($result))
{
	$array[$count] = array(array("order_id" => $row[2], "user_id" => $row[3], "title" => $row[4], "description" => $row[5], "publisher" => $row[6], "address" => $row[7], "image_url" => $row[8], "publish_date" =>$row[9], "remain_time" =>$row[10], "contact" =>$row[11], "reward" => $row[12], "status" => $row[13]));
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
