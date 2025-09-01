<?php
    header('Content-Type: application/json; charset=utf-8');
    $recv = json_decode(file_get_contents('php://input'), true);

    $s = new PDO("mysql:host=localhost;dbname=Restaurant", "xxxx", "xxxx");

    $o_id = $recv['o_id'];
    
    $re = $s->query("UPDATE order_basic SET o_state = 1 WHERE o_id = $o_id");

?>