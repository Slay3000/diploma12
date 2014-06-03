<?php

$response = array();
 
if (isset($_POST['photo_address'])) {
    $paddress = $_POST['photo_address'];
 
    require 'db_connect.php';
 
    $db = new DB_CONNECT();
  
 $path = 'img/';
     unlink($path . $paddress);
	
   $deletephotodb = mysql_query("DELETE FROM photos WHERE photo_address = $paddress");

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