<?php
$response = array();
 
if (isset($_POST['id'])) {
 
    $id = $_POST['id'];
 

    require 'db_connect.php';
 
    $db = new DB_CONNECT();
     $result = mysql_query("UPDATE items SET qrcode='tmc$id' WHERE id=$id");
	
 
    if ($result) {
        $response["success"] = 1;
        $response["message"] = "Product successfully created.";
		
 
        echo json_encode($response);
    } else {
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";
 
        echo json_encode($response);
    }}

?>