<?php
 
$response = array();
if (isset($_POST['id'])) {
 
    $id = $_POST['id'];
require 'db_connect.php';
 
$db = new DB_CONNECT();
 
$result = mysql_query("SELECT photo_address FROM photos WHERE item_id=$id") or die(mysql_error());
 
if (mysql_num_rows($result) > 0) {
    $response["img"] = array();
 
    while ($row = mysql_fetch_array($result)) {
        $img = array();
		          $img["photo_address"] = $row["photo_address"];
          
        array_push($response["img"], $img);
    }
    $response["success"] = 1;
 
    echo json_encode($response);
} else {
    $response["success"] = 0;
    $response["message"] = "No images found";
 
    echo json_encode($response);
}}
?>