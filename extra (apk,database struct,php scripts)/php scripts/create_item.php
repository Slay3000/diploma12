<?php
$response = array();
 
if (isset($_POST['name']) && isset($_POST['location']) && isset($_POST['inumber']) && isset($_POST['description']) &&isset($_POST['catname'])) {
 
    $name = urldecode($_POST['name']);
    $location = urldecode($_POST['location']);
    $inumber = urldecode($_POST['inumber']);
	 $description = urldecode($_POST['description']);
	 $category = urldecode($_POST['catname']);
    require 'db_connect.php';
 
    $db = new DB_CONNECT();
 
    $result = mysql_query("INSERT INTO items(name, location, inumber, description,catname) VALUES('$name', '$location', '$inumber', '$description','$category')");
 
    if ($result) {
        $response["success"] = 1;
        $response["message"] = "Product successfully created.";
 
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