package uc.jarvis;

import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class PostSensorDataTask extends AsyncTask<String, Void, Void> {
    @Override
    protected Void doInBackground(String... params) {

        String url = "http://192.168.178.10:8080/sensor-data";

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"), params[0]);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Log.i("UPDATE", request.toString());
        try {
            Response response = client.newCall(request).execute();
            Log.i("UPDATE", response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
