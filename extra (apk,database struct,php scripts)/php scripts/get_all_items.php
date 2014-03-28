<?php
 
$response = array();

require 'db_connect.php';
 
$db = new DB_CONNECT();
 
$result = mysql_query("SELECT *FROM items") or die(mysql_error());
 
if (mysql_num_rows($result) > 0) {
    $response["items"] = array();
 
    while ($row = mysql_fetch_array($result)) {
        $product = array();
        $product["id"] = $row["id"];
        $product["name"] = $row["name"];
  
 
        array_push($response["items"], $product);
    }
    $response["success"] = 1;
 
    echo json_encode($response);
} else {
    $response["success"] = 0;
    $response["message"] = "No products found";
 
    echo json_encode($response);
}
?>