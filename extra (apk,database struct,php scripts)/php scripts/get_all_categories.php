<?php
 
$response = array();

require 'db_connect.php';
 
$db = new DB_CONNECT();
 
$result = mysql_query("SELECT *FROM category") or die(mysql_error());
 
if (mysql_num_rows($result) > 0) {
    $response["category"] = array();
 
    while ($row = mysql_fetch_array($result)) {
        $category = array();
        $category["id"] = $row["id"];
        $category["catname"] = $row["catname"];
  
 
        array_push($response["category"], $category);
    }
    $response["success"] = 1;
 
    echo json_encode($response);
} else {
    $response["success"] = 0;
    $response["message"] = "No products found";
 
    echo json_encode($response);
}
?>