package to.msn.wings.restaurant6;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SimpleService extends Service {
    private final String URI_LIST_ORDER_TO_COOK = "http://xxx.xxx.xxx.xxx/restaurant/list_orderToCook.php";
    private ScheduledExecutorService schedule;
    public static final String ACTION = "SimpleService Action";
    private String str;

    // データの受信
    Network dl = new Network() {
        @Override
        public String accessURI() {
            return URI_LIST_ORDER_TO_COOK;
        }
        @Override
        public String onSend() {
            return " ";
        }
        @Override
        public void onResponse(String result) {
            str = result;
        }
    };

    // サービス初回起動時に実行
    @Override
    public void onCreate(){ super.onCreate(); }

    // サービス起動毎回実行
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        schedule = Executors.newSingleThreadScheduledExecutor();
        schedule.scheduleWithFixedDelay(() -> {
            dl.execute();  // データの受信
            Intent i = new Intent(ACTION);
            i.putExtra("str", str);
            sendBroadcast(i);
        }, 0, 3000, TimeUnit.MILLISECONDS);
        return START_STICKY;
    }

    // サービスをバインド時に実行
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // サービス停止時に実行
    @Override
    public void onDestroy() {
        super.onDestroy();
        schedule.shutdown();
    }
}