package to.msn.wings.restaurant6;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SimpleReceiver extends BroadcastReceiver {
    // レシーバーの登録
    @Override
    public void onReceive(Context context, Intent intent) {
        String result = intent.getStringExtra("str");

        if(result == null) return;  // もし前処理でDBアクセスが間に合わずnullが返ってきた時はreturn

        ArrayList<AlreadyListItem> list = new ArrayList<>();
        try{
            // 読み込んだ文字列からJSON配列を生成
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
                list.add(item);
            }
        } catch (JSONException e){
            Log.d("NetworkGui", "\n受信データ読み込み失敗");
            e.printStackTrace();
        }

        AlreadyOrderListAdapter alreadyOrderListAdapter = new AlreadyOrderListAdapter(context, list, R.layout.already_order_list);
        ListView lv = ((KitchenActivity)context).findViewById(R.id.listOrderedFood);
        lv.setAdapter(alreadyOrderListAdapter);
        lv.setSelection(list.size() - 1);

    }
}
