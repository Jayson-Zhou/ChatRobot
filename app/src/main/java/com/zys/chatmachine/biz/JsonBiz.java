package com.zys.chatmachine.biz;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.zys.chatmachine.bean.ResponseResult;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Json解析类
 * Created by zm678 on 2019/3/16.
 */

public class JsonBiz implements IJsonBiz {

    //图灵机器人API_key;
    private static final String API_KEY = "4a868c35b5d04adc947299198e222a93";

    //图灵机器人用户ID
    private static final String USER_ID = "325828";

    private JsonBiz() {
    }

    public static final JsonBiz getInstance() {
        return InstanceHolder.JsonBizInstance;
    }

    private static class InstanceHolder {
        private static final JsonBiz JsonBizInstance = new JsonBiz();
    }

    @Override
    public String Json2String(String JsonMessage) throws JsonSyntaxException {
        ResponseResult responseResult;
        String response;
        responseResult = new Gson().fromJson(JsonMessage, ResponseResult.class);
        response = responseResult.getResults().get(0).getValues().getText();
        return response;
    }

    @Override
    public String String2Json(String inputMessage) {
        return getJson(inputMessage);
    }

    /**
     * 用于将用户输入的文本转换为JSON格式
     *
     * @param inputMessage
     * @return JsonMessage
     */
    private String getJson(String inputMessage) {
        JSONObject reqJson = null;
        try {
            reqJson = new JSONObject();

            reqJson.put("reqType", 0);

            JSONObject perception = new JSONObject();
            JSONObject inputText = new JSONObject();
            inputText.put("text", inputMessage);
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
