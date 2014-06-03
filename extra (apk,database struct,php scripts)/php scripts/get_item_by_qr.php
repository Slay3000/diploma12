<?php
 
$response = array();
if (isset($_POST['qrcode'])) {
 
    $qrcode = $_POST['qrcode'];
require 'db_connect.php';
 
$db = new DB_CONNECT();
 
$result = mysql_query("SELECT * FROM items WHERE qrcode ='$qrcode'") or die(mysql_error());
 
if (mysql_num_rows($result) > 0) {
    $response["item"] = array();
 
    while ($row = mysql_fetch_array($result)) {
        $item = array();
		          $item["id"] = $row["id"];
               $item["name"] = $row["name"];
            $item["location"] = $row["location"];
			 $item["inumber"] = $row["inumber"];
            $item["description"] = $row["description"];
            $item["catname"] = $row["catname"];
        array_push($response["item"], $item);
    }
    $response["success"] = 1;
 
    echo json_encode($response);
} else {
    $response["success"] = 0;
    $response["message"] = "No products found";
 
    echo json_encode($response);
}}
?>