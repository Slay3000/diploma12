<?php

require 'db_connect.php';
 
$db = new DB_CONNECT();
 
$result = mysql_query("SELECT * FROM photos where item_id=22") or die(mysql_error());
 $path = 'img/';
if (mysql_num_rows($result) > 0) {
   
    while ($row = mysql_fetch_array($result)) {
      unlink($path . $row["photo_address"]);
	}}

?>