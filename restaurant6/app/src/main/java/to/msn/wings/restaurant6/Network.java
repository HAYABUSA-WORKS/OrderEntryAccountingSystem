package to.msn.wings.restaurant6;

import static android.os.Looper.getMainLooper;

import android.util.Log;

import androidx.core.os.HandlerCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

public interface Network {
    // 通信先のアドレスを指定するメソッド
    String accessURI();
    // 送信データを生成するメソッド
    String onSend();
    // 受信データを反映するメソッド
    void onResponse(String result);
    // 通信を行うメソッド
    default void execute(){
        StringBuilder result = new StringBuilder();

        Executors.newSingleThreadExecutor().execute(() -> {
            try{
                String uri = accessURI();
                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
                con.setDoOutput(true);

                OutputStream os = con.getOutputStream();
                PrintStream ps = new PrintStream(os);
                String msg = onSend();
                ps.print(msg);
                ps.close();

                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
                String line;
                while ((line = reader.readLine()) != null){
                    result.append(line);
                }

                HandlerCompat.createAsync(getMainLooper()).post(() -> {
                    onResponse(result.toString());
                });
            } catch (IOException e){
                Log.d("NetworkGui", "\n通信処理失敗");
                e.printStackTrace();
            }
        });
    }
}
