package cn.o4a.jmh;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersionDetector;
import com.networknt.schema.ValidationMessage;

import java.io.IOException;
import java.util.Set;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/8/1 9:49
 */
public class JSONSchemaValidator {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static JsonNode getJson(String content) throws IOException {
        return mapper.readTree(content);
    }


    public static Set<ValidationMessage> valid(String schema, String json) {
        try {
            final JsonNode jsonNode = getJson(schema);
            JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersionDetector.detect(jsonNode));
            final JsonSchema jsonSchema = factory.getSchema(jsonNode);
            jsonSchema.initializeValidators();
            return jsonSchema.validate(getJson(json));
        } catch (Exception e) {
            //
            throw new RuntimeException(e);
        }

    }
}
