<?php

$response = array();
 
if (isset($_POST['id'])) {
    $id = $_POST['id'];
 
    require 'db_connect.php';
 
    $db = new DB_CONNECT();
   $deletefiles = mysql_query("SELECT * FROM photos where item_id= $id");
 $path = 'img/';
if (mysql_num_rows($deletefiles) > 0) {
   
    while ($row = mysql_fetch_array($deletefiles)) {
      unlink($path . $row["photo_address"]);
	}}
    $deleteitem = mysql_query("DELETE FROM items WHERE id = $id");
	$deletephotodb = mysql_query("DELETE FROM photos WHERE item_id = $id");

    if (mysql_affected_rows() > 0) {
        $response["success"] = 1;
        $response["message"] = "Item successfully deleted";
 
        echo json_encode($response);
    } else {
        $response["success"] = 0;
        $response["message"] = "No item found";
 
        echo json_encode($response);
    }
} else {
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    echo json_encode($response);
}
?>