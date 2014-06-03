<?php

$response = array();
 
if (isset($_POST['id'])) {
    $id = $_POST['id'];
 
    require 'db_connect.php';
 
    $db = new DB_CONNECT();
 
    $result = mysql_query("DELETE FROM category WHERE id = $id");
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