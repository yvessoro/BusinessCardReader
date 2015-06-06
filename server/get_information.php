<?php

/*
 * Following code will list all the informations
 */

// array for JSON response
$response = array();
 
// include db connect class
	include_once 'db_functions.php';
	$db = new DB_Functions();
 
// get all informations from informations table
$employeeID=$_GET["id"];
$result = $db->getEmployeeByID($employeeID);

// check for empty result
if (mysql_num_rows($result) > 0) {
    // looping through all results
    // informations node
    $response["information"] = array();
 
    while ($row = mysql_fetch_array($result)) {
        // temp user array
        $infos = array();
        $infos["id"] = mb_convert_encoding($row["id"], "UTF-8", "Windows-1252");
		$infos["lastname"] = mb_convert_encoding($row["lastname"], "UTF-8", "Windows-1252");
		$infos["firstname"] = mb_convert_encoding($row["firstname"], "UTF-8", "Windows-1252");
 
        // push single product into final response array
        array_push($response["information"], $infos);
    }
    // success
    $response["success"] = 1;
 
    // echoing JSON response
    echo json_encode($response);
} else {
    // no products found
    $response["success"] = 0;
    $response["message"] = "this employee ID does not exist";
 
    // echo no users JSON
    echo json_encode($response);
	
	//echo json_encode($response);
}
?>