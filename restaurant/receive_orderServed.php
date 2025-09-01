<?php
    header('Content-Type: application/json; charset=utf-8');
    $recv = json_decode(file_get_contents('php://input'), true);

    $s = new PDO("mysql:host=localhost;dbname=Restaurant", "xxxx", "xxxx");

    $od_id = $recv['od_id'];
    
    $re = $s->query("UPDATE order_detail SET od_state = 3 WHERE od_id = $od_id");

?>