package com.zys.chatmachine.biz;

/**
 * Json解析类接口
 * Created by zm678 on 2019/3/16.
 */

public interface IJsonBiz {

    String Json2String(String JsonMessage);

    String String2Json(String inputMessage);
}
