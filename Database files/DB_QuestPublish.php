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
$user_id = "";
$title = "";
$description = "";
$publisher = "";
$address = "";
$image_url = "";
$publish_date = "";
$expired_date = "";
$contact = "";
$reward = "";
$status = "";
$array = array();
$count = 1;
if (isset($_POST["itsc"]) && !empty(trim($_POST["itsc"])))
{
    $itsc = mysqli_real_escape_string($connection, trim($_POST["itsc"]));
}
if (isset($_POST["title"]) && !empty(trim($_POST["title"])))
{
    $title = mysqli_real_escape_string($connection, trim($_POST["title"]));
}
if (isset($_POST["description"]) && !empty(trim($_POST["description"])))
{
    $description = mysqli_real_escape_string($connection, trim($_POST["description"]));
}
if (isset($_POST["publisher"]) && !empty(trim($_POST["publisher"])))
{
    $publisher = mysqli_real_escape_string($connection, trim($_POST["publisher"]));
}
if (isset($_POST["publish_date"]) && !empty(trim($_POST["publish_date"])))
{
    $publish_date = mysqli_real_escape_string($connection, trim($_POST["publish_date"]));
}
if (isset($_POST["expired_date"]) && !empty(trim($_POST["expired_date"])))
{
    $expired_date = mysqli_real_escape_string($connection, trim($_POST["expired_date"]));
}
if (isset($_POST["contact"]) && !empty(trim($_POST["contact"])))
{
    $contact = mysqli_real_escape_string($connection, trim($_POST["contact"]));
}
if (isset($_POST["reward"]) && !empty(trim($_POST["reward"])))
{
    $reward = mysqli_real_escape_string($connection, trim($_POST["reward"]));
}
$query =  "SELECT `user_id` FROM user WHERE `itsc` = '".$itsc."';";
$result = mysqli_query($connection, $query);
if($row = mysqli_fetch_assoc($result))
{
	$user_id = $row['user_id'];
	$insert = "INSERT INTO orders (`user_id`, `title`, `description`, `publisher`, `publish_date`, `expired_date`, `contact`, `reward`, `status`) VALUES ('".$user_id."', '".$title."', '".$description."', '".$publisher."', '".$publish_date."', '".$expired_date."', '".$contact."', '".$reward."', '0')";
	if (mysqli_query($connection, $insert)) 
	{
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