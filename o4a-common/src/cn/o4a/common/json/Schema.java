package cn.o4a.common.json;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/7/26 9:56
 */
public class Schema  {
    private final String content;
    private int curosr = 0;

    private final Element root;
    private final boolean ordered;


    private Schema(String schemaText, boolean ordered) {
        this.ordered = ordered;
        this.root = new Element(null, null);
        this.content = schemaText;

    }


    public static Schema of(String schemaText) {

        final char[] chars = schemaText.toCharArray();


        final Schema schema = new Schema(schemaText, true);
        final Element root = schema.getRoot();


        return schema;
    }

    private Element constructElement(String exp) {

        //while (true) {
        //    final char c = content[curosr];
        //    if (content.length > 1) {
        //
        //    }
        //}


        return new Element(null, null, ordered);
    }


    private char charAt(int idx) {
        return 'a';
    }
    public String toStandardString() {
        return "";
    }

    public String toCompressString() {
        return "";
    }

    public Element getRoot() {
        return root;
    }

    public static void main(String[] args) {
        final Schema schema = Schema.of("{\n" +
                "    property: <string>(min =1, max =5, required = true);\n" +
                "    object: <object>(){\n" +
                "        propertya:<number>();\n" +
                "        propertya:<bool>();\n" +
                "    };\n" +
                "    \n" +
                "    array:<array>();\n" +
                "\n" +
                "    arrayO:<array>()[\n" +
                "        {\n" +
                "            propertya:<number>();\n" +
                "            propertyb:<bool>();\n" +
                "        }\n" +
                "    ];\n" +
                "}");
        final String standerdString = schema.toStandardString();
        final String compressString = schema.toCompressString();


        System.out.println((int)'{');
    }
}
