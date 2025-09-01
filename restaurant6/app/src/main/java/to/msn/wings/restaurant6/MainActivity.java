package to.msn.wings.restaurant6;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final String URI_LIST_SEATS = "http://xxx.xxx.xxx.xxx/restaurant/list_seats.php";
    private final String URI_LIST_FOODS = "http://xxx.xxx.xxx.xxx/restaurant/list_foods.php";
    private final String URI_RECEIVE_ORDER_DETAIL = "http://xxx.xxx.xxx.xxx/restaurant/receive_order_detail.php";
    private final String URI_RECEIVE_ORDER_BASIC = "http://xxx.xxx.xxx.xxx/restaurant/receive_order_basic.php";
    private final String URI_LIST_ORDER_DETAIL = "http://xxx.xxx.xxx.xxx/restaurant/list_order_detail.php";
    private final String URI_LIST_ORDER_DETAIL2 = "http://xxx.xxx.xxx.xxx/restaurant/list_order_detail2.php";
    private final String URI_RECEIVE_ORDER_SERVED = "http://xxx.xxx.xxx.xxx/restaurant/receive_orderServed.php";
    private final String URI_RECEIVE_ORDER_PAID = "http://xxx.xxx.xxx.xxx/restaurant/receive_orderPaid.php";

    ArrayList<SeatItem> seatItemList;  // データベースから引っ張ってきた席リスト
    ArrayList<FoodItem> foodItemList;  // データベースから引っ張ってきた商品リスト
    ArrayList<ListItem> orderList;  // 注文リストのデータ
    ArrayList<AlreadyListItem> alreadyOrderList;  // 注文済リストのデータ


    ArrayAdapter<SeatItem> seatAdapter;  // 席選択スピナーのアダプター
    OrderListAdapter orderListAdapter;  // 注文リストのアダプター
    AlreadyOrderListAdapter alreadyOrderListAdapter;  // 注文済リストのアダプター

    int seatPos = 0;  // 選択された席
    int foodPos = 0;  // 選択された商品
    int quantityPos = 0;  // 選択された数量
    int changePos = 0;  // 変更しようと注文リストの押された番号（index）
    int changeServedPos = 0;  // stateを変更しようと注文済リストの押された番号（index）

    boolean isButtonAdd = true;  // 「追加／変更」ボタン切り替え用
    boolean isScrollTop = false;  // 「注文照会」ボタンを押した時に自動で注文済リストがトップに戻ったのか項目がタップされたのか判定用

    Spinner spSeat;  // 席選択スピナー
    Spinner spFood;  // 食品選択スピナー
    Spinner spQuantity;  // 数量選択スピナー
    EditText edNote;  // 備考欄テキスト
    ListView lvOrderList;  // 注文リスト
    ListView lvAlreadyOrderList;  // 注文済リスト

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spSeat= findViewById(R.id.spSeat);
        spFood = findViewById(R.id.spFood);
        spQuantity = findViewById(R.id.spQuantity);
        edNote = findViewById(R.id.edNote);
        lvOrderList = findViewById(R.id.listOrder);  // 注文リスト
        lvAlreadyOrderList = findViewById(R.id.listAlreadyOrder);  // 注文済リスト

        // orderListとそのアダプターを生成
        orderList = new ArrayList<>();
        orderListAdapter = new OrderListAdapter(this, orderList, R.layout.order_list);
        lvOrderList.setAdapter(orderListAdapter);
        // alreadyOrderListとそのアダプターを生成
        alreadyOrderList = new ArrayList<>();
        alreadyOrderListAdapter = new AlreadyOrderListAdapter(MainActivity.this, alreadyOrderList, R.layout.already_order_list);
        lvAlreadyOrderList.setAdapter(alreadyOrderListAdapter);

        // NW1 席選択スピナーの生成／更新
        Network seatDl = new Network() {
            @Override
            public String accessURI() {
                return URI_LIST_SEATS;
            }
            @Override
            public String onSend() {
                return " ";
            }
            @Override
            public void onResponse(String result) {
                seatItemList = new ArrayList<>();
                SeatItem first = new SeatItem();
                first.setO_id(-1);
                seatItemList.add(first);
                try {
                    JSONArray ary = new JSONArray(result);
                    for (int i = 0; i < ary.length(); i++) {
                        JSONObject ln = ary.getJSONObject(i);
                        SeatItem item = new SeatItem();
                        item.setS_id(ln.getString("s_id"));
                        item.setS_capacity(ln.getInt("s_capacity"));
                        item.setO_id(ln.getInt("o_id"));
                        seatItemList.add(item);
                    }
                } catch (JSONException e) {
                    Log.d("RestaurantError", "\n受信データ読み込み失敗");
                    e.printStackTrace();
                }

                seatAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, seatItemList);
                Spinner seatSpn = findViewById(R.id.spSeat);
                seatSpn.setAdapter(seatAdapter);
                spSeat.setSelection(seatPos);
            }
        };
        seatDl.execute();

        // NW2 食品選択スピナーの生成
        new Network() {
            @Override
            public String accessURI() { return URI_LIST_FOODS; }
            @Override
            public String onSend() { return " "; }
            @Override
            public void onResponse(String result) {
                foodItemList = new ArrayList<>();
                try{
                    JSONArray ary = new JSONArray(result);
                    for(int i = 0; i < ary.length(); i++){
                        JSONObject ln = ary.getJSONObject(i);
                        FoodItem item = new FoodItem();
                        item.setIndex(i);
                        item.setF_id(ln.getString("f_id"));
                        item.setF_name(ln.getString("f_name"));
                        item.setF_price(ln.getInt("f_price"));
                        foodItemList.add(item);
                    }
                } catch (JSONException e) {
                    Log.d("RestaurantError", "\n受信データ読み込み失敗");
                    e.printStackTrace();
                }
                ArrayAdapter<FoodItem> foodAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, foodItemList);
                Spinner foodSpn = findViewById(R.id.spFood);
                foodSpn.setAdapter(foodAdapter);
            }
        }.execute();

        // NW3 order_detailテーブルから注文詳細データの受信：注文済リストを更新
        Network orderDetailDl = new Network() {
            @Override
            public String accessURI() { return URI_LIST_ORDER_DETAIL2; }
            @Override
            public String onSend() {
                JSONObject jobj = new JSONObject();
                try {
                    jobj.put("o_id", seatItemList.get(seatPos).getO_id());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jobj.toString();
            }
            @Override
            public void onResponse(String result) {
                alreadyOrderList.clear();
                try{
                    JSONArray ary = new JSONArray(result);

                    if(ary.length() == 0) return;  // DBから取得したデータが空の時はreturn

                    // 注文済リストの項目を生成
                    int i;
                    for(i = 0; i < ary.length() - 1; i++){
                        JSONObject ln = ary.getJSONObject(i);
                        AlreadyListItem item = new AlreadyListItem();
                        item.setIndex(i);
                        item.setAli_id(ln.getString("od_f_id"));
                        item.setAli_name(ln.getString("f_name"));
                        item.setAli_price(ln.getInt("f_price"));
                        item.setAli_quantity(ln.getInt("od_quantity"));
                        item.setAli_note(ln.getString("od_memo"));
                        item.setAli_time(ln.getString("od_time"));
                        item.setAli_state(ln.getInt("od_state"));
                        item.setAli_order_id(ln.getInt("od_id"));
                        alreadyOrderList.add(item);

                    }
                    // 最終行に合計金額の項目を生成
                    JSONObject ln = ary.getJSONObject(i);
                    AlreadyListItem item = new AlreadyListItem();
                    item.setIndex(i);
                    item.setAli_name(getString(R.string.txtSum));
                    item.setAli_price(ln.getInt("f_price"));
                    alreadyOrderList.add(item);

                } catch (JSONException e) {
                    Log.d("RestaurantError", "\n注文詳細データ読み込み失敗");
                    e.printStackTrace();
                }
                alreadyOrderListAdapter.notifyDataSetChanged();
                lvAlreadyOrderList.setSelection(0);
                isScrollTop = true;
            }
        };

        // NW4 order_detailテーブルに書き込み更新　「注文送信」ボタンを押す
        Network orderSd = new Network() {
            @Override
            public String accessURI() { return URI_RECEIVE_ORDER_DETAIL; }
            @Override
            public String onSend() {
                JSONArray ary = new JSONArray();
                try {
                    for (int i = 0; i < orderList.size(); i++) {
                        ListItem item = orderList.get(i);
                        JSONObject jobj = new JSONObject();
                        jobj.put("od_o_id", seatItemList.get(seatPos).getO_id());
                        jobj.put("od_f_id", item.getLi_id());
                        jobj.put("od_quantity", item.getLi_quantity());
                        jobj.put("od_memo", item.getLi_note());
                        ary.put(jobj);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return ary.toString();
            }
            @Override
            public void onResponse(String result) {
                orderList.clear();  // 注文リストをクリア
                orderListAdapter.notifyDataSetChanged();  // 注文リストの表示を更新
                orderDetailDl.execute();  // 注文済リストを更新
            }
        };

        // NW5 order_basicテーブルの更新：選択した席が空きから食事中に更新
        Network vacantSeatSd = new Network() {
            @Override
            public String accessURI() { return URI_RECEIVE_ORDER_BASIC; }
            @Override
            public String onSend() {
                JSONObject jobj = new JSONObject();
                try {
                    jobj.put("o_s_id", seatItemList.get(seatPos).getS_id());
                    jobj.put("o_state", 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jobj.toString();
            }
            @Override
            public void onResponse(String result) {
                seatDl.execute(); // 席選択スピナーを更新
            }
        };

        // NW6 注文済リストのstateを配膳済へ更新
        Network stateAlreadyServedSd = new Network() {
            @Override
            public String accessURI() { return URI_RECEIVE_ORDER_SERVED; }
            @Override
            public String onSend() {
                JSONObject jobj = new JSONObject();
                try {
                    jobj.put("od_id", alreadyOrderList.get(changeServedPos).getAli_order_id());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jobj.toString();
            }
            @Override
            public void onResponse(String result) {
                orderDetailDl.execute();
            }
        };

        // NW7 order_basicテーブルのo_stateを更新：会計状態を「支払い済(1)」に更新
        Network checkSd = new Network() {
            @Override
            public String accessURI() { return URI_RECEIVE_ORDER_PAID; }
            @Override
            public String onSend() {
                JSONObject jobj = new JSONObject();
                try {
                    jobj.put("o_id", seatItemList.get(seatPos).getO_id());
                } catch (JSONException e){
                    e.printStackTrace();
                }
                return jobj.toString();
            }
            @Override
            public void onResponse(String result) {
                seatPos = 0;  // 「会計」ボタンを押した時に席スピナーの表示を初期に戻すため
                seatDl.execute();
            }
        };

        // 数量選択スピナーの生成
        ArrayList<Integer> quantityList = new ArrayList<>();
        for(int i = 0; i <= 15; i++){
            quantityList.add(i);
        }
        ArrayAdapter<Integer> quantityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, quantityList);
        spQuantity.setAdapter(quantityAdapter);
        spQuantity.setSelection(1);

        // 席選択スピナーを選択した時の処理
        spSeat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                seatPos = position;
                orderDetailDl.execute();  // 注文済リストを更新

                if(seatItemList.get(seatPos).getO_id() != 0) return;  // すでに食事中の席または「タップして席を選択」を選んだ時：order_basicテーブルを更新しない＝なにもしない

                vacantSeatSd.execute();  // order_basicテーブルを更新：選択した席が空きから食事中に更新
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // 食品選択スピナーを選択した時の処理
        spFood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                foodPos = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // 数量選択スピナーを選択した時の処理
        spQuantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                quantityPos = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // 追加／変更ボタンを押した時の処理
        Button btnAddChange = findViewById(R.id.btnAddChange);
        btnAddChange.setOnClickListener(v -> {
            // 「追加」ボタンの時
            if(isButtonAdd) {
                if (seatPos == 0) {  // 席が選択されていない時：トースト表示
                    Toast.makeText(this, R.string.toastNoSeat, Toast.LENGTH_LONG).show();
                } else {
                    if(quantityPos == 0){  // 数量選択が0の時：トースト表示
                        Toast.makeText(this, R.string.toastNoQuantity, Toast.LENGTH_LONG).show();
                    } else {
                        ListItem item = new ListItem();
                        FoodItem food = foodItemList.get(foodPos);
                        item.setIndex(food.getIndex());
                        item.setLi_id(food.getF_id());
                        item.setLi_name(food.getF_name());
                        item.setLi_price(food.getF_price());
                        item.setLi_quantity(quantityPos);
                        item.setLi_note(edNote.getText().toString());
                        orderList.add(item);

                        orderListAdapter.notifyDataSetChanged();
                    }
                }
                // 「変更」ボタンの時
            } else {
                if(quantityPos == 0) {  // 数量選択が0の時：その項目を注文リストから削除
                    orderList.remove(changePos);
                } else {  // 数量選択が0以外の時：登録内容の更新
                    ListItem item = new ListItem();
                    FoodItem food = foodItemList.get(foodPos);
                    item.setIndex(food.getIndex());
                    item.setLi_id(food.getF_id());
                    item.setLi_name(food.getF_name());
                    item.setLi_price(food.getF_price());
                    item.setLi_quantity(quantityPos);
                    item.setLi_note(edNote.getText().toString());
                    orderList.set(changePos, item);
                }
                btnAddChange.setText(getString(R.string.btnAdd));
                isButtonAdd = true;
                // 注文リストの項目すべての背景色を白にリセット
                for(int i = 0; i < lvOrderList.getChildCount(); i++){
                    lvOrderList.getChildAt(i).setBackgroundColor(Color.WHITE);
                }
                orderListAdapter.notifyDataSetChanged();  // ビューを更新する
            }
        });

        // 注文リストの項目を押した時の処理
        lvOrderList.setOnItemClickListener((av, v, pos, id) -> {
            // 項目すべての背景色を白にリセット
            for(int i = 0; i < av.getChildCount(); i++){
                av.getChildAt(i).setBackgroundColor(Color.WHITE);
            }
            v.setBackgroundColor(Color.GRAY);  // 選択した項目をグレーバックに変更
            ListItem item = orderList.get(pos);
            spFood.setSelection(item.getIndex());
            foodPos = item.getIndex();
            spQuantity.setSelection(item.getLi_quantity());
            quantityPos = item.getLi_quantity();
            edNote.setText(item.getLi_note());
            btnAddChange.setText(getString(R.string.btnChange));
            isButtonAdd = false;
            changePos = pos;
        });

        // 「注文送信」ボタンを押した時の処理
        Button btnOrderSend = findViewById(R.id.btnOrderSend);
        btnOrderSend.setOnClickListener(v -> {
            orderSd.execute();
        });

        // 「空席照会」ボタンを押した時の処理
        Button btnVacantSeat = findViewById(R.id.btnVacantSeat);
        btnVacantSeat.setOnClickListener(v -> {
            seatDl.execute();  // 席選択スピナーを更新
            spSeat.performClick();  // 席選択スピナーを開く
        });

        // 「注文照会」ボタンを押した時の処理
        Button btnOrderInquire = findViewById(R.id.btnOrderInquire);
        btnOrderInquire.setOnClickListener(v -> {
            orderDetailDl.execute();
        });

        // 注文済リストの項目を押した時の処理
        lvAlreadyOrderList.setOnItemClickListener((av, v, pos, id) -> {
            // タップした項目が「調理済」以外の時：なにもしない
            if(alreadyOrderList.get(pos).getAli_state() != 2) return;

            ((TextView)v.findViewById(R.id.aoTxtState)).setText(getString(R.string.txtOdState3));
            changeServedPos = pos;
            stateAlreadyServedSd.execute();
        });

        // 「会計」ボタンを押した時の処理
        Button btnCheck = findViewById(R.id.btnCheck);
        btnCheck.setOnClickListener(v -> {
            if(!checkServeState()){
                Toast.makeText(this, getString(R.string.toastNotServeYet), Toast.LENGTH_LONG).show();
            } else {
                checkSd.execute();
                // 注文済リストをクリアして表示を空にする
                alreadyOrderList.clear();
                AlreadyListItem item = new AlreadyListItem();
                item.setAli_name("合計");
                item.setAli_price(0);
                alreadyOrderList.add(item);
                alreadyOrderListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override  // オプションメニューの生成
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(this, KitchenActivity.class);
        startActivity(intent);
        return true;
    }

    private boolean checkServeState(){
        for(int i = 0; i < alreadyOrderList.size() - 1; i++) {
            if(alreadyOrderList.get(i).getAli_state() != 3){
                return false;
            }
        }
        return true;
    }
}