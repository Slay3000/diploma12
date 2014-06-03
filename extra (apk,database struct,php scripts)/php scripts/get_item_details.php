<?php
 
$response = array();
 
require 'db_connect.php';
 
$db = new DB_CONNECT();
 
if (isset($_GET["id"])) {
    $id = $_GET['id'];
 
    $result = mysql_query("SELECT * FROM items WHERE id = $id");
 
    if (!empty($result)) {
        if (mysql_num_rows($result) > 0) {
 
            $result = mysql_fetch_array($result);
 
            $item = array();
            $item["id"] = $result["id"];
            $item["name"] = $result["name"];
            $item["location"] = $result["location"];
			 $item["inumber"] = $result["inumber"];
            $item["description"] = $result["description"];
			$item["catname"] = $result["catname"];
           
            
            $response["success"] = 1;
 
            $response["item"] = array();
 
            array_push($response["item"], $item);
 
            echo json_encode($response);
        } else {
            $response["success"] = 0;
            $response["message"] = "No item found";
 
            echo json_encode($response);
        }
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