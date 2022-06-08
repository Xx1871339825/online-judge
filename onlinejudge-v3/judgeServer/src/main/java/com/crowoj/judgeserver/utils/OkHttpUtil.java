package com.crowoj.judgeserver.utils;

import cn.hutool.core.map.MapUtil;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author crow
 * @create 2022/2/19 0:46
 * @description
 */
public class OkHttpUtil {
    private static final OkHttpClient client = new OkHttpClient();

    private static String resolver(ResponseBody responseBody) {
        InputStream is = null;
        String result = null;
        try {
            is = responseBody.byteStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String body = null;
            StringBuilder sb = new StringBuilder();
            while ((body = br.readLine()) != null) {
                sb.append(body);
            }
            is.close();
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    public static String postData(String url, String json, Map<String, String> headerMap) throws IOException {
        var requestBody = RequestBody.create(json,MediaType.parse("application/json; charset=utf-8"));
        var builder = new Request.Builder().url(url);
        if (MapUtil.isNotEmpty(headerMap)){
            headerMap.forEach(builder::addHeader);
        }
        var request = builder.post(requestBody).build();
        var body = client.newCall(request).execute().body();
        return resolver(body);
    }

    public static void delete(String url){
        var builder = new Request.Builder().url(url);
        var request = builder.delete().build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.err.println(response);
            }
        });
    }

}
