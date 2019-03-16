package com.zys.chatmachine.biz;

import com.google.gson.JsonSyntaxException;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 网络连接类
 * Created by zm678 on 2019/3/16.
 */

public class ConnectBiz implements IConnectBiz {

    //图灵机器人URL
    private static final String API_URL = "http://openapi.tuling123.com/openapi/api/v2";

    public OnConnectListener mOnConnectListener;
    public IJsonBiz jsonBiz;

    @Override
    public void onConnect(String inputMessage) {
        String response = "";
        BufferedReader in = null;
        BufferedOutputStream out = null;
        HttpURLConnection connection = null;

        jsonBiz = JsonBiz.getInstance();
        String reqJson = jsonBiz.String2Json(inputMessage);

        Boolean isSucceed;
        try {
            URL url = new URL(API_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setReadTimeout(8 * 1000);
            connection.setConnectTimeout(8 * 1000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            byte[] b = reqJson.getBytes();
            out = new BufferedOutputStream(connection.getOutputStream(), 8 * 1024);
            out.write(b);
            out.flush();

            //建立连接
            connection.connect();

            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder str = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                str.append(line);
            }

            response = jsonBiz.Json2String(str.toString());
            isSucceed = true;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            mOnConnectListener.connectFail();
            isSucceed = false;
        } catch (IOException e) {
            e.printStackTrace();
            mOnConnectListener.connectFail();
            isSucceed = false;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            mOnConnectListener.connectFail();
            isSucceed = false;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }

        if (isSucceed) {
            mOnConnectListener.connectSuccess(response);
        }
    }

    @Override
    public void setOnConnectListener(OnConnectListener onConnectlistener) {
        mOnConnectListener = onConnectlistener;
    }
}
