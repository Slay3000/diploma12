<?php
$response = array();
 
if (isset($_POST['itemid']) && isset($_POST['photoaddr']) ) {

    $itemid = $_POST['itemid'];
 
	 $photoname = $_POST['photoaddr'];

	$imgi= $photoname.".jpg";

    require 'db_connect.php';
 
    $db = new DB_CONNECT();
 
    $result = mysql_query("INSERT INTO photos(item_id, photo_address) VALUES('$itemid', '$imgi ')");
 
    if ($result) {
        $response["success"] = 1;
        $response["message"] = "Photo successfully added.";
 
        echo json_encode($response);
    } else {
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";
 
        echo json_encode($response);
    }
} else {
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    echo json_encode($response);
}
?>