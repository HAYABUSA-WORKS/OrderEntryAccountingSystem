<?php
    header('Content-Type: application/json; charset=utf-8');
    $recv = json_decode(file_get_contents('php://input'), true);

    $s = new PDO("mysql:host=localhost;dbname=Restaurant", "xxxx", "xxxx");

    foreach($recv as $rows){
        
        $od_o_id = $rows['od_o_id'];
        $od_f_id = $rows['od_f_id'];
        $od_quantity = $rows['od_quantity'];
        $od_memo = $rows['od_memo'];

        $re = $s->query("INSERT INTO order_detail (od_o_id, od_f_id, od_quantity, od_memo, od_time, od_state) 
                            VALUES ('$od_o_id', '$od_f_id', '$od_quantity', '$od_memo', NOW(), 1)");
    }
?>