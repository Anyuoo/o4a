package cn.o4a.common.json.field;

import com.alibaba.fastjson.JSONObject;

public interface Assertion {

    void assertJson(Condition condition, JSONObject json, String jsonKey);
}