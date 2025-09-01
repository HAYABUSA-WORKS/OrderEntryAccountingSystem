<?php
    $s = new PDO("mysql:host=localhost;dbname=Restaurant", "xxxx", "xxxx");
    $re = $s->query("SELECT s_id, s_capacity, coalesce(o_id, 0) AS o_id FROM seats AS s LEFT OUTER JOIN (SELECT * FROM order_basic WHERE o_state = 0) AS o ON s.s_id = o.o_s_id");
    $rows = array();
    while($row = $re->fetch(PDO::FETCH_ASSOC)){
        $rows[] = $row;
    }
    header('Content-Type: application/json; charset=utf-8');
    echo json_encode($rows);
?>