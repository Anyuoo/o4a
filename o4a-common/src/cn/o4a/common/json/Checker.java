package cn.o4a.common.json;

import cn.o4a.common.json.field.Condition;
import cn.o4a.common.json.field.FieldType;
import com.alibaba.fastjson.JSON;

import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

import static java.util.Map.Entry;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/7/26 11:25
 */
public class Checker {

    public static void validate(Schema schema, String json) throws IllegalJsonValException {

        final Element root = schema.getRoot();

        final JSON data = (JSON)JSON.parse(json);


        final PriorityQueue<ElementData> elementQueue = new PriorityQueue<>();
        elementQueue.add(new ElementData(Collections.singletonList(root), data));

        while (!elementQueue.isEmpty()) {
            final ElementData elementData = elementQueue.poll();
            final JSON jsonO = elementData.json;

            for (Element element : elementData.elements) {
                for (Entry<String, Element> elementEntry : element.entrySet()) {
                    final String key = elementEntry.getKey();
                    final Element value = elementEntry.getValue();
                    final FieldType fieldType = value.getFieldType();
                    final Condition condition = value.getCondition();
                    //fieldType.assertJson(condition, null, key);


                    elementQueue.add(new ElementData((List<Element>) value.values(), jsonO));
                }

            }
        }
    }

    public boolean isLegalJson(String json) {
        return false;
    }

    public static void main(String[] args) {
        Checker.validate(Schema.of(""), "");
    }

    static class ElementData{
        List<Element> elements;
        JSON json;

        public ElementData(List<Element> elements, JSON json) {
            this.elements = elements;
            this.json = json;
        }
    }
}
