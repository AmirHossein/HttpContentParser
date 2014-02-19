import java.util.HashMap;

/**
 * User: amirhossein
 * Date: 2014/02/18 - 11:30
 */
public class HttpRequestParser {
    protected String request = null;
    protected String method = null;
    protected String version = null;
    protected String URI = null;
    protected HashMap<String, String> headers = null;
    protected String body = null;

    private String[] sections;
    private String[] identifiers;
    private String[] headerFields;

    public HttpRequestParser(String request) {
        this.request = request;
        this.parse();
    }

    private void parse() {
        if (this.validate()) {
            this.parseIdentifier(this.identifiers);
            this.parseHeaderFields(this.headerFields);
            this.parseBody(shift_array(this.sections));
        }
    }

    public boolean validate() {
        if (this.request != null) {
            this.request = this.request.trim();
            if (!this.request.isEmpty()) {
                String[] sections = this.request.split("\\n\\n");
                if (sections.length > 0 && !sections[0].isEmpty()) {

                    String[] headers = sections[0].split("\\n");
                    if (headers[0].length() > 1) {
                        String[] identifiers = headers[0].split(" ");
                        if (identifiers.length == 3) {
                            this.sections = sections;
                            this.identifiers = identifiers;
                            this.headerFields = shift_array(headers);
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    protected void parseIdentifier(String[] identifiers) {
        this.method = identifiers[0];
        this.URI = identifiers[1];
        this.version = identifiers[2];
    }

    protected void parseHeaderFields(String[] lines) {
        this.headers = new HashMap<String, String>();

        for (String line : lines) {
            String[] field = line.split(":");
            String key = field[0].trim();

            field = shift_array(field);
            StringBuilder fieldValue = new StringBuilder();
            for (String aField : field) {
                fieldValue.append(" ").append(aField);
            }
            if (fieldValue.length() > 0) {
                fieldValue.delete(0, 1);
            }

            this.headers.put(key, fieldValue.toString().trim());
        }
    }

    protected void parseBody(String[] sections) {
        if (sections.length > 0) {
            StringBuilder bodyContent = new StringBuilder();
            for (String section : sections) {
                bodyContent.append("\n\n").append(section);
            }
            if (bodyContent.length() > 0) {
                bodyContent.delete(0, 2);
            }

            this.body = bodyContent.toString();
        }
    }

    public String getMethod() {
        return method;
    }

    public String getURI() {
        return URI;
    }

    public String getVersion() {
        return version;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public String getHeader(String key) {
        return this.headers.get(key);
    }

    public String getBody() {
        return body;
    }

    private static String[] shift_array(String[] array) {
        if (array.length > 0) {
            String[] newArray = new String[array.length - 1];
            for (int i = 1; i < array.length; i += 1) {
                newArray[i - 1] = array[i];
            }
            return newArray;
        } else {
            return array;
        }
    }
}
