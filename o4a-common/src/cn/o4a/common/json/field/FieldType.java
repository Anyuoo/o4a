package cn.o4a.common.json.field;

import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/7/26 9:56
 */
public enum FieldType implements Assertion {
    /**
     *
     */
    STRING {
        @Override
        public void assertJson(Condition condition, JSONObject json, String jsonKey) {
            final String val = json.getString(jsonKey);
        }
    },

    ARRAY {
        @Override
        public void assertJson(Condition condition, JSONObject json, String jsonKey) {
            //

        }
    },

    OBJECT {
        @Override
        public void assertJson(Condition condition, JSONObject json, String jsonKey) {
            //
        }
    },

    NUMBER {
        @Override
        public void assertJson(Condition condition, JSONObject json, String jsonKey) {
            final BigDecimal bigDecimal = json.getBigDecimal(jsonKey);

        }
    },

    BOOL {
        @Override
        public void assertJson(Condition condition, JSONObject json, String jsonKey) {
            //
            condition.isNullable();
            json.containsKey(jsonKey);
            json.getBoolean(jsonKey);
        }
    },
    ;


    public static FieldType match(String keyword) {
        for (FieldType fieldType : values()) {
            if (fieldType.name().equals(keyword)) {
                return fieldType;
            }
        }
        throw new RuntimeException("");
    }


}

