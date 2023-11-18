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
$query =  "SELECT * FROM orders;";
$result = mysqli_query($connection, $query);
$array = array();
$count = 0;
while($row = mysqli_fetch_array($result))
{
	$array[$count++] = array($row[0], $row[1], $row[2], $row[3], $row[4], $row[5], $row[6], $row[7], $row[8]);
}
echo json_encode($array);
mysqli_free_result($result);
mysqli_close($connection);
exit();
?>