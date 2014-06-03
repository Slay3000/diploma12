<?php
 
$response = array();
if (isset($_POST['login']) && isset($_POST['password']) && isset($_POST['permission'])) {
$name = $_POST['login'];
$password = $_POST['password'];
$permission = $_POST['permission'];
require 'db_connect.php';
 
$db = new DB_CONNECT();

$result = mysql_query("SELECT * FROM  users WHERE name='$name' AND password='$password' AND permission='$permission'") or die(mysql_error());

if (mysql_num_rows($result) > 0) {

    $response["success"] = 1;
 
    echo json_encode($response);
} else {
    $response["success"] = 0;
    $response["message"] = "No products found";
 
    echo json_encode($response);
}}
else {
  $response["success"] = 0;
    $response["message"] = "Data error";
	   echo json_encode($response);
	}
?>