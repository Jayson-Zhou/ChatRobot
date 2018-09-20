package com.zys.chatmachine;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Jayson on 2018/9/19.
 * 网络连接工具类，试图与图灵机器人API建立连接
 */

public class HttpUtils {

    //图灵机器人URL
    private static final String API_URL = "http://openapi.tuling123.com/openapi/api/v2";

    //图灵机器人API_key;
    private static final String API_KEY = "4a868c35b5d04adc947299198e222a93";

    //图灵机器人用户ID
    private static final String USER_ID = "325828";


    /**
     * 发送消息给图灵机器人
     *
     * @param inputMessage
     * @return
     */
    public static String sendMessage(String inputMessage) {

        String response = "";
        BufferedReader in = null;
        BufferedOutputStream out = null;
        HttpURLConnection connection = null;

        String reqJson = getJson(inputMessage);

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

            connection.connect();

            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder str = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                str.append(line);
            }

            response = str.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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

        return response;
    }

    /**
     * 用于将用户输入的文本转换为JSON格式
     *
     * @param message
     * @return
     */
    private static String getJson(String message) {
        JSONObject reqJson = null;
        try {
            reqJson = new JSONObject();

            reqJson.put("reqType", 0);

            JSONObject perception = new JSONObject();
            JSONObject inputText = new JSONObject();
            inputText.put("text", message);
            perception.put("inputText", inputText);

            JSONObject userInfo = new JSONObject();
            userInfo.put("apiKey", API_KEY);
            userInfo.put("userId", USER_ID);

            reqJson.put("perception", perception);
            reqJson.put("userInfo", userInfo);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reqJson.toString();
    }

}
