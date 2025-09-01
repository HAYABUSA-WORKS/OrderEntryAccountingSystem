<?php
    header('Content-Type: application/json; charset=utf-8');
    $recv = json_decode(file_get_contents('php://input'), true);

    $o_id = $recv['o_id'];

    $s = new PDO("mysql:host=localhost;dbname=Restaurant", "xxxx", "xxxx");

    $re = $s->query("SELECT od_f_id, f_name, f_price, od_quantity, od_memo, od_time, od_state, od_id
                        FROM order_detail AS od
                        LEFT OUTER JOIN foods AS f
                        ON od.od_f_id = f.f_id
                        WHERE od_o_id = $o_id");
    $rows = array();
    while($row = $re->fetch(PDO::FETCH_ASSOC)){
        $rows[] = $row;
    }

    $re = $s->query("SELECT CAST(SUM(f_price * od_quantity) AS SIGNED) AS f_price
                        FROM order_detail AS od
                        LEFT OUTER JOIN foods AS f
                        ON od.od_f_id = f.f_id
                        WHERE od_o_id = $o_id
                        GROUP BY od_o_id");

    while($row = $re->fetch(PDO::FETCH_ASSOC)){
        $rows[] = $row;
    }

    header('Content-Type: application/json; charset=utf-8');
    echo json_encode($rows);
?>