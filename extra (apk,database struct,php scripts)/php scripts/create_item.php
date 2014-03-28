<?php
$response = array();
 
if (isset($_POST['name']) && isset($_POST['location']) && isset($_POST['inumber']) && isset($_POST['description'])) {
 
    $name = $_POST['name'];
    $location = $_POST['location'];
    $inumber = $_POST['inumber'];
	 $description = $_POST['description'];

    require 'db_connect.php';
 
    $db = new DB_CONNECT();
 
    $result = mysql_query("INSERT INTO items(name, location, inumber, description) VALUES('$name', '$location', '$inumber', '$description')");
 
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