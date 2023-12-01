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
$student_id = "";
$itsc = "";
$email = "";
$password = "";
if (isset($_POST["student_id"]) && !empty(trim($_POST["student_id"])))
{
    $student_id = mysqli_real_escape_string($connection, trim($_POST["student_id"]));
}
if (isset($_POST["itsc"]) && !empty(trim($_POST["itsc"])))
{
    $itsc = mysqli_real_escape_string($connection, trim($_POST["itsc"]));
}
if (isset($_POST["email"]) && !empty(trim($_POST["email"])))
{
    $email = mysqli_real_escape_string($connection, trim($_POST["email"]));
}
if (isset($_POST["password"]) && !empty(trim($_POST["password"])))
{
    $password = mysqli_real_escape_string($connection, trim($_POST["password"]));
}


    $insert = "INSERT INTO user (`student_id`, `itsc`, `email`, `password`) VALUES ('".$student_id."', '".$itsc."', '".$email."', '".$password."');";
    if (mysqli_query($connection, $insert)) 
    {
        echo "Data inserted successfully!";
    } 
    else 
    {
        echo "Error in the query: " . mysqli_error($connection);
    }

mysqli_close($connection);
exit();
?>