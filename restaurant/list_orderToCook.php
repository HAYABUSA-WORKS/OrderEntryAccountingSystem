<?php
    $s = new PDO("mysql:host=localhost;dbname=Restaurant", "xxxx", "xxxx");

    $re = $s->query("SELECT od_f_id, f_name, f_price, od_quantity, od_memo, od_time, od_state, o_s_id, od_id
                        FROM order_basic AS ob
                        RIGHT OUTER JOIN order_detail AS od
                        ON ob.o_id = od.od_o_id
                        LEFT OUTER JOIN foods AS f
                        ON od.od_f_id = f.f_id
                        WHERE od_state = 1");
                        
    $rows = array();
    while($row = $re->fetch(PDO::FETCH_ASSOC)){
        $rows[] = $row;
    }

    header('Content-Type: application/json; charset=utf-8');
    echo json_encode($rows);
?>