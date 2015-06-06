<?php

class DB_Functions {

    private $db;

    //put your code here
    // constructor
    function __construct() {
        include_once './db_connect.php';
        // connecting to database
        $this->db = new DB_Connect();
        $this->db->connect();
    }

    // destructor
    function __destruct() {
        
    }
    /**
     * Get employee by ID
     */
    public function getEmployeeByID($ID) {
        $result = mysql_query("select * FROM employee where id='$ID'");
        return $result;
    }

}

?>