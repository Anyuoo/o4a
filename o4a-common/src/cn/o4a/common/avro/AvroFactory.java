package cn.o4a.common.avro;

import cn.o4a.common.Assert;
import cn.o4a.common.ClassUtil;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/7/29 9:35
 */
public class AvroFactory {
    public static final String AVRO_FILE_FORMAT = ".avro";
    /**
     * schema 声明
     */
    private static final String SCHEMA_DEFINE = "{\n" +
            "\t\"type\": \"record\",\n" +
            "\t\"name\": \"$name\",\n" +
            "\t\"fields\": [\n" +
            "$fields" +
            "\t]\n" +
            "}";
    /**
     * 字段声明
     */
    private static final String FIELD_DEFINE = "\t\t{\"name\": \"%s\", \"type\":[\"%s\", \"null\"]}";

   <T> void  doWrite(Path fileName, AvroWriter<T> writer) {


    }

    /**
     * 构建schema json字符串
     *
     * @return schema json
     */
    public static String buildSchemaJson(Class<?> schemaClass) {

        final StringBuilder fields = new StringBuilder();

        final AvroEntity avroEntity = schemaClass.getAnnotation(AvroEntity.class);
        Assert.notNull(avroEntity, "");

        String schemaName = Optional.ofNullable(avroEntity.value())
                .filter(val -> !val.equals(""))
                .orElse(schemaClass.getSimpleName());

        final List<Field> declaredFields = ClassUtil.listAllDeclaredFields(schemaClass);
        final int size = declaredFields.size();
        for (int i = 0; i < size; i++) {
            final Field field = declaredFields.get(i);
            final AvroField avroField = field.getAnnotation(AvroField.class);
            fields.append(String.format(FIELD_DEFINE, avroField.value(), avroField.type()));

            if (i < size - 1) {
                fields.append(",\n");
            } else {
                fields.append("\n");
            }
        }

        return SCHEMA_DEFINE.replace("$name", schemaName)
                .replace("$fields", fields.toString());
    }
}
