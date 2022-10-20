package cn.o4a.common.json.field;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/7/26 10:29
 */
public class Condition {


    private Long max;
    private Long min;
    private String pattern;
    private boolean nullable;
    private boolean required;


    public Condition(String exp) {

    }

    public Long getMax() {
        return max;
    }

    public void setMax(Long max) {
        this.max = max;
    }

    public Long getMin() {
        return min;
    }

    public void setMin(Long min) {
        this.min = min;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
