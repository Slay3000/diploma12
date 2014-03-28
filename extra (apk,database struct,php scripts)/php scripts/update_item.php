<?php
 
$response = array();
 
if (isset($_POST['id']) && isset($_POST['name']) && isset($_POST['location']) && isset($_POST['inumber']) && isset($_POST['description'])) {
 
    $id = $_POST['id'];
    $name = $_POST['name'];
    $location = $_POST['location'];
	 $inumber = $_POST['inumber'];
    $description = $_POST['description'];
 
    require 'db_connect.php';
 
    $db = new DB_CONNECT();
 
    $result = mysql_query("UPDATE items SET name = '$name', location = '$location', inumber = '$inumber',description = '$description' WHERE id = $id");
 
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