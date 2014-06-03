<?php
$response = array();
 
if (isset($_POST['catname'])) {
 
    $catname = $_POST['catname'];
  

    require 'db_connect.php';
 
    $db = new DB_CONNECT();
 
    $result = mysql_query("INSERT INTO category(catname) VALUES('$catname')");
 
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