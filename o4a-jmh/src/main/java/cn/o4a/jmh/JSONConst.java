package cn.o4a.jmh;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/8/1 9:52
 */
public interface JSONConst {

    String JSON_1 = "{\n" +
            "  \"productId\": 1,\n" +
            "  \"productName\": \"A green door\",\n" +
            "  \"price\": 12.50,\n" +
            "  \"tags\": [ \"home\", \"green\" ]\n" +
            "}";

    String JSON_SCHENA_1 = "{\n" +
            "  \"$schema\": \"https://json-schema.org/draft/2020-12/schema\",\n" +
            "  \"$id\": \"https://example.com/product.schema.json\",\n" +
            "  \"title\": \"Product\",\n" +
            "  \"description\": \"A product from Acme's catalog\",\n" +
            "  \"type\": \"object\",\n" +
            "  \"properties\": {\n" +
            "    \"productId\": {\n" +
            "      \"description\": \"The unique identifier for a product\",\n" +
            "      \"type\": \"integer\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"required\": [ \"productId\" ]\n" +
            "}";
}
