<?php
 
$response = array();
 
if (isset($_POST['id']) && isset($_POST['name']) && isset($_POST['location']) && isset($_POST['inumber']) && isset($_POST['description'])&& isset($_POST['catname'])) {
 
    $id = $_POST['id'];
    $name = urldecode($_POST['name']);
    $location = urldecode($_POST['location']);
	 $inumber = $_POST['inumber'];
    $description =urldecode( $_POST['description']);
	$catname =urldecode( $_POST['catname']);
 
    require 'db_connect.php';
 
    $db = new DB_CONNECT();
 
    $result = mysql_query("UPDATE items SET name = '$name', location = '$location', inumber = '$inumber',description = '$description',catname = '$catname' WHERE id = $id");
 
    if ($result) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "Item successfully updated.";
 
        echo json_encode($response);
    } else {
 
    }
} else {
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    echo json_encode($response);
}
?>