package to.msn.wings.restaurant6;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class KitchenActivity extends AppCompatActivity {
    private final String URI_LIST_ORDER_TO_COOK = "http://xxx.xxx.xxx.xxx/restaurant/list_orderToCook.php";
    private final String URI_RECEIVE_ORDER_COOKED = "http://xxx.xxx.xxx.xxx/restaurant/receive_orderCooked.php";
    Intent sv;  // サービスへの参照フィールド

    boolean isScroll = true;  // スクロール中（＝レシーバーが作動しているか）の判定用
    int listPos = 0;  // 受注食品リストの選択された項目の位置
    ArrayList<AlreadyListItem> orderedFoodList;  // 受注食品リスト
    ListView lvOrderedFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen);

        // オプションメニューに「戻る」を表示
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        lvOrderedFood = findViewById(R.id.listOrderedFood);

        // 受注食品リストの生成
        new Network() {
            @Override
            public String accessURI() { return URI_LIST_ORDER_TO_COOK; }
            @Override
            public String onSend() { return " "; }
            @Override
            public void onResponse(String result) {
                orderedFoodList = new ArrayList<>();
                try{
                    JSONArray ary = new JSONArray(result);
                    for(int i = 0; i < ary.length(); i++){
                        JSONObject ln = ary.getJSONObject(i);
                        AlreadyListItem item = new AlreadyListItem();
                        item.setIndex(i);
                        item.setAli_id(ln.getString("od_f_id"));
                        item.setAli_name(ln.getString("f_name"));
                        item.setAli_price(ln.getInt("f_price"));
                        item.setAli_quantity(ln.getInt("od_quantity"));
                        item.setAli_note(ln.getString("od_memo"));
                        item.setAli_seatNo(ln.getString("o_s_id"));
                        item.setAli_state(ln.getInt("od_state"));
                        item.setAli_time(ln.getString("od_time"));
                        item.setAli_order_id(ln.getInt("od_id"));
                        orderedFoodList.add(item);
                    }
                } catch (JSONException e) {
                    Log.d("RestaurantError", "\n受信データ読み込み失敗");
                    e.printStackTrace();
                }
                AlreadyOrderListAdapter alreadyOrderListAdapter = new AlreadyOrderListAdapter(KitchenActivity.this, orderedFoodList, R.layout.already_order_list);
                lvOrderedFood.setAdapter(alreadyOrderListAdapter);
            }
        }.execute();

        // 受注食品リストのstateを調理済へ更新
        Network stateAlreadyCookedSd = new Network() {
            @Override
            public String accessURI() { return URI_RECEIVE_ORDER_COOKED; }
            @Override
            public String onSend() {
                JSONObject jobj = new JSONObject();
                try {
                    jobj.put("od_id", orderedFoodList.get(listPos).getAli_order_id());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jobj.toString();
            }
            @Override
            public void onResponse(String result) {

            }
        };

        Button btnScroll = findViewById(R.id.btnScroll);
        btnScroll.setOnClickListener(v -> {
            if(isScroll){
                btnScroll.setText(getString(R.string.txtStartScroll));
                isScroll = false;
                stopService(sv);
            } else {
                btnScroll.setText(getString(R.string.txtStopScroll));
                isScroll = true;
                startService(sv);
            }
        });

        // レシーバーの登録
        SimpleReceiver receiver = new SimpleReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(SimpleService.ACTION);
        registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED);
        // SimpleServiceを起動
        sv = new Intent(this, SimpleService.class);
        startService(sv);

        // 受注食品リストの項目を押した時の処理
        lvOrderedFood.setOnItemClickListener((av, v, pos, id) -> {
            ((TextView)v.findViewById(R.id.aoTxtState)).setText(getString(R.string.txtOdState2));
            listPos = pos;
            stateAlreadyCookedSd.execute();
        });
    }

    // オプションメニューの「戻る」を押した時の処理：KitchenActivityを破棄してMainActivityに戻る
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(sv);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(sv);
    }
}