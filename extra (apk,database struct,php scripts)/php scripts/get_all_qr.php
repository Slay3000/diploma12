<?php
 
$response = array();

require 'db_connect.php';
 
$db = new DB_CONNECT();
 
$result = mysql_query("SELECT id,qrcode FROM items WHERE qrcode <>''") or die(mysql_error());
 
if (mysql_num_rows($result) > 0) {
    $response["codes"] = array();
 
    while ($row = mysql_fetch_array($result)) {
        $codes = array();
		          $codes["qrcode"] = $row["qrcode"];
   
        array_push($response["codes"], $codes);
    }
    $response["success"] = 1;
 
    echo json_encode($response);
} else {
    $response["success"] = 0;
    $response["message"] = "No products found";
 
    echo json_encode($response);
}
?>