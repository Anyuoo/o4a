package cn.o4a.common.json;

import cn.o4a.common.json.field.Condition;
import cn.o4a.common.json.field.FieldType;

import java.util.*;

/**
 *  name        type            condition
 *  property: <string>(min =1, max =5, required = true);
 *
 * @author Anyu
 * @version 1.0.0
 * @since 2022/7/26 10:32
 */
public class Element implements Map<String, Element> {

    private final Condition condition;
    private final FieldType fieldType;
    private final Map<String, Element> sonElement;

    public Element(FieldType fieldType, Condition condition, boolean ordered) {
        this.condition = condition;
        this.fieldType = fieldType;
        this.sonElement = ordered ? new LinkedHashMap<>() : new HashMap<>();
    }

    public Condition getCondition() {
        return condition;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public Element(FieldType fieldType, Condition condition) {
        this(fieldType, condition, false);
    }



    @Override
    public int size() {
        return sonElement.size();
    }

    @Override
    public boolean isEmpty() {
        return sonElement.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return sonElement.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return sonElement.containsValue(value);
    }

    @Override
    public Element get(Object key) {
        return sonElement.get(key);
    }

    @Override
    public Element put(String key, Element value) {
        return sonElement.put(key, value);
    }

    @Override
    public Element remove(Object key) {
        return sonElement.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Element> elements) {
        sonElement.putAll(elements);
    }

    @Override
    public void clear() {
        sonElement.clear();
    }

    @Override
    public Set<String> keySet() {
        return sonElement.keySet();
    }

    @Override
    public Collection<Element> values() {
        return sonElement.values();
    }

    @Override
    public Set<Entry<String, Element>> entrySet() {
        return sonElement.entrySet();
    }
}
