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
$password = "";
$result = "";
if (isset($_POST["itsc"]) && !empty(trim($_POST["itsc"])))
{
    $itsc = mysqli_real_escape_string($connection, trim($_POST["itsc"]));
}
if (isset($_POST["password"]) && !empty(trim($_POST["password"])))
{
    $password = mysqli_real_escape_string($connection, trim($_POST["password"]));
}
$query =  "SELECT `itsc`, `password` FROM user 
WHERE `itsc` = '".$itsc."' AND `password` = '".$password."';";

if(isset($_POST["itsc"]) && isset($_POST["password"]))
{
	$result = mysqli_query($connection, $query);
	$count = 0;
	while ($note = mysqli_fetch_assoc($result)) {
		$count += 1;
	}
	if ($count == 1)
	{
		echo "The record is found.";
	}
	else
	{
		echo "The record is not found.";
	}
}

mysqli_free_result($result);
mysqli_close($connection);

exit();

?>