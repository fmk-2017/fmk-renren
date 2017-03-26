package com.example.everyoneassist.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by fengm on 2017-3-4.
 */

public class HttpPostRequestUtils {

    private HttpPostRequestCallback hprc;

    public HttpPostRequestUtils(HttpPostRequestCallback hprc) {
        this.hprc = hprc;
    }

    public static HttpPostRequestUtils getInstance(HttpPostRequestCallback hprc) {
        HttpPostRequestUtils hpru = new HttpPostRequestUtils(hprc);
        return hpru;
    }

    public void Post(HashMap<String, String> map) {
        final String method = map.get("act");
        RequestParams rp = new RequestParams(Constant.Url);
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            String value = map.get(key);
            if (key.contains("image"))
                rp.addBodyParameter(key, new File(value));
            else rp.addBodyParameter(key, value);
        }
        Callback.Cancelable cc = x.http().post(rp, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if (x.isDebug()) {
                        Log.e(method + "====", result.toString());
                        Toast.makeText(hprc.getContext(), result.getString("info"), Toast.LENGTH_SHORT).show();
                    }
                    if ("success".equals(result.getString("result"))) hprc.Success(method, result);
                    else
                        Toast.makeText(hprc.getContext(), result.getString("info"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (x.isDebug())
                    hprc.Fail(method, ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                hprc.Fail(method, "用户取消");
            }

            @Override
            public void onFinished() {
                Log.i("HttpPostRequestUtils", "onFinished---------->请求完成" + method);
            }
        });
    }

    public interface HttpPostRequestCallback {
        void Success(String method, JSONObject json) throws JSONException;

        void Fail(String method, String error);

        Context getContext();
    }


}
