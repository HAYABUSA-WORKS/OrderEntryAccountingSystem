<?php
    header('Content-Type: application/json; charset=utf-8');
    $recv = json_decode(file_get_contents('php://input'), true);

    $s = new PDO("mysql:host=localhost;dbname=Restaurant", "xxxx", "xxxx");

    $o_s_id = $recv['o_s_id'];
    $o_state = $recv['o_state'];
    
    $re = $s->query("INSERT INTO order_basic (o_date, o_s_id, o_state) 
                        VALUES (NOW(), '$o_s_id', '$o_state')");

?>