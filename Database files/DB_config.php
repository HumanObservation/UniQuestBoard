<?php 
class DB_config
{
    private $dbhost;
    private $dbuser;
    private $dbpass;
    private $dbname;
    public function __construct() 
    {
        $this->dbhost = "localhost";
        $this->dbuser = "root";
        $this->dbpass = "";
        $this->dbname = "uniquestboard";
    }
    
    public function __get($value)
    {
        switch ($value)
        {
        case("dbhost"):
            return $this->dbhost;
            break;
        case("dbuser"):
            return $this->dbuser;
            break;
        case("dbpass"):
            return $this->dbpass;
            break;
        case("dbname"):
            return $this->dbname;
            break;
        default:
            throw new Exception("Invalid access.");
            break;
        }
    }
}

?>